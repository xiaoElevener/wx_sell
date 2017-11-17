package com.xiao.wx_order.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.xiao.wx_order.converter.OrderMaster2OrderDTOConverter;
import com.xiao.wx_order.dto.CartDTO;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.entity.OrderDetail;
import com.xiao.wx_order.entity.OrderMaster;
import com.xiao.wx_order.entity.ProductInfo;
import com.xiao.wx_order.enums.OrderStatusEnum;
import com.xiao.wx_order.enums.PayStatusEnum;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.repository.OrderDetailRepository;
import com.xiao.wx_order.repository.OrderMasterRepository;
import com.xiao.wx_order.service.OrderService;
import com.xiao.wx_order.service.PayService;
import com.xiao.wx_order.service.ProductService;
import com.xiao.wx_order.service.PushMessage;
import com.xiao.wx_order.service.WebSocket;
import com.xiao.wx_order.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private OrderMasterRepository orderMasterRepository;

	@Autowired
	private PayService payService;

	@Autowired
	private PushMessage pushMessageService;
	
	@Autowired
	private WebSocket webSocket;

	@Override
	public OrderDTO create(OrderDTO orderDTO) {
		BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
		String orderId = KeyUtil.getUniqueKey();
		// List<CartDTO> cartDTOList = new ArrayList<CartDTO>();
		// 1.查询商品（数量，价格）
		for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
			ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
			if (productInfo == null) {
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			// 2.计算总价
			orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
					.add(orderAmount);
			// 3.1 orderDetail订单详情入库
			BeanUtils.copyProperties(productInfo, orderDetail);
			orderDetail.setDetailId(KeyUtil.getUniqueKey());
			orderDetail.setOrderId(orderId);
			orderDetailRepository.save(orderDetail);
			// CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),
			// orderDetail.getProductQuantity());
			// cartDTOList.add(cartDTO);
		}
		// 3.2写入订单主表数据库
		OrderMaster orderMaster = new OrderMaster();
		orderDTO.setOrderId(orderId);
		BeanUtils.copyProperties(orderDTO, orderMaster);
		orderMaster.setOrderAmount(orderAmount);
		orderMaster.setOrderStatus(OrderStatusEnum.NEW.code);
		orderMaster.setPayStatus(PayStatusEnum.WAIT.code);
		orderMasterRepository.save(orderMaster);

		// 4.扣库存
		List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
				.map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
		productService.decreaseStock(cartDTOList);
		
		//发送websocket消息
		webSocket.sendMessage(orderDTO.getOrderId());
		
		return orderDTO;
	}

	@Override
	public OrderDTO findOne(String orderId) {
		OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
		if (orderMaster == null) {
			throw new SellException(ResultEnum.ORDER_NOT_EXIST);
		}
		List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
		if (orderDetailList == null) {
			throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
		}

		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(orderMaster, orderDTO);
		orderDTO.setOrderDetailList(orderDetailList);
		return orderDTO;
	}

	@Override
	public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
		Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
		List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
		Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
		return orderDTOPage;
	}

	@Override
	@Transactional
	public OrderDTO cancle(OrderDTO orderDTO) {
		OrderMaster orderMaster = new OrderMaster();
		// 判断订单状态
		if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.code)) {
			log.error("【取消订单】订单状态不正确,orderId={},orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}

		// 修改订单状态
		orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.code);
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);
		if (updateResult == null) {
			log.error("【取消订单】更新失败,orderMaster={}", orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		// 返还库存
		if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
			log.error("【取消订单】订单中无商品详情，orderDTO={}", orderDTO);
			throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
		}

		List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
				.map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
		productService.increaseStock(cartDTOList);

		// 如果已支付退款,需要退款
		if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.code)) {
			payService.refund(orderDTO);
		}
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO finish(OrderDTO orderDTO) {
		// 判断订单状态
		if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.code)) {
			log.error("【完结订单】订单状态不正确,orderId={},orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}

		// TODO 完结订单要不要判断订单以支付

		// 修改订单状态
		orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.code);
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);

		if (updateResult == null) {
			log.error("【完结订单】更新失败，orderMaster={}", orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}

		// 推送模板消息(由于微信白名单限制，完成不了)
		// pushMessageService.orderStatus(orderDTO);
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO paid(OrderDTO orderDTO) {
		// 判断订单状态
		if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.code)) {
			log.error("【订单支付成功】订单状态不正确,orderId={},orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}

		// 判断支付状态
		if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.code)) {
			log.error("【订单支付完成】订单支付状态不正确，orderDTO={}", orderDTO);
			throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
		}
		// 修改支付状态
		orderDTO.setPayStatus(PayStatusEnum.SUCCESS.code);
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);
		if (updateResult == null) {
			log.error("【订单支付完成】更新失败，orderMaster={}", orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		return orderDTO;
	}

	@Override
	public Page<OrderDTO> findList(Pageable pageable) {
		Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
		List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
		Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
		return orderDTOPage;
	}

}
