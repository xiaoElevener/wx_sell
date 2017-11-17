package com.xiao.wx_order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.entity.OrderDetail;
import com.xiao.wx_order.enums.OrderStatusEnum;
import com.xiao.wx_order.enums.PayStatusEnum;
import com.xiao.wx_order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceTest {
	@Autowired
	private OrderService orderService;

	private final String BUYEROPENID = "xiao123";

	@Test
	public void create() {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setBuyerName("饭饭");
		orderDTO.setBuyerAddress("赣州");
		orderDTO.setBuyerPhone("11011011011");
		orderDTO.setBuyerOpenid(BUYEROPENID);

		// 购物车
		List<OrderDetail> orderDetailList = new ArrayList<>();

		OrderDetail o1 = new OrderDetail();
		o1.setProductId("654321");
		o1.setProductQuantity(4);
		orderDetailList.add(o1);

		OrderDetail o2 = new OrderDetail();
		o2.setProductId("123456");
		o2.setProductQuantity(2);
		orderDetailList.add(o2);

		orderDTO.setOrderDetailList(orderDetailList);

		OrderDTO result = orderService.create(orderDTO);
		log.info("【创建订单】 result = {}", result);
		Assert.assertNotNull(result);
	}

	@Test
	public void findOneTest() {
		OrderDTO result = orderService.findOne("1508914718684570781");
		log.info("【查询单个订单】result={}", result);
		Assert.assertNotNull(result);
	}

	@Test
	public void findList() {
		PageRequest request = new PageRequest(0, 2);
		Page<OrderDTO> orderDTOPage = orderService.findList(BUYEROPENID, request);
		Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
	}

	@Test
	public void cancelTest() {
		OrderDTO result = orderService.findOne("1508914718684570781");
		OrderDTO orderDTO = orderService.cancle(result);
		Assert.assertEquals(OrderStatusEnum.CANCEL.code, orderDTO.getOrderStatus());
	}

	@Test
	public void finishTest() {
		OrderDTO result = orderService.findOne("1508914718684570781");
		OrderDTO orderDTO = orderService.finish(result);
		Assert.assertEquals(OrderStatusEnum.FINISHED.code, orderDTO.getOrderStatus());
	}

	@Test
	public void paidTest() {
		OrderDTO result = orderService.findOne("1508914718684570781");
		OrderDTO orderDTO = orderService.paid(result);
		Assert.assertEquals(orderDTO.getPayStatus(), PayStatusEnum.SUCCESS.code);
	}

	@Test
	public void list() {
		PageRequest request = new PageRequest(0, 2);
		Page<OrderDTO> orderDTOPage = orderService.findList(request);
		Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
	}

}
