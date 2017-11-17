package com.xiao.wx_order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiao.wx_order.VO.ResultVO;
import com.xiao.wx_order.converter.OrderForm2OrderDTOConverter;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.form.OrderForm;
import com.xiao.wx_order.service.BuyerService;
import com.xiao.wx_order.service.OrderService;
import com.xiao.wx_order.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private BuyerService buyerService;

	// 创建订单
	@PostMapping("/create")
	public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {

		log.info("【创建订单】orderForm={}", orderForm);

		if (bindingResult.hasErrors()) {
			log.error("【创建订单】参数不正确，errorForm={}", orderForm);
			throw new SellException(ResultEnum.PARAM_ERROR.code, bindingResult.getFieldError().getDefaultMessage());
		}
		OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
		if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
			log.error("【创建订单】购物车不能为空");
			throw new SellException(ResultEnum.CART_EMPTY);
		}

		OrderDTO createResult = orderService.create(orderDTO);
		Map<String, String> map = new HashMap<>();
		map.put("orderId", createResult.getOrderId());
		log.info("【创建订单】createResult={}", createResult);
		log.info("【创建订单】orderId={}", createResult.getOrderId());

		return ResultVOUtil.success(map);
	}

	// 订单列表
	@PostMapping("/list")
	public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		if (StringUtils.isEmpty(openid)) {
			log.error("【查询订单列表】openid为空");
			throw new SellException(ResultEnum.PARAM_ERROR);
		}

		PageRequest request = new PageRequest(page, size);

		Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);
		return ResultVOUtil.success(orderDTOPage.getContent());
	}

	// 订单详情
	@GetMapping("/detail")
	public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
		OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
		return ResultVOUtil.success(orderDTO);
	}

	// 取消订单
	@PostMapping("/cancel")
	public ResultVO cancel(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
		log.info("【买家取消订单】orderId={}", orderId);
		buyerService.cancelOrder(openid, orderId);
		return ResultVOUtil.success();
	}

}
