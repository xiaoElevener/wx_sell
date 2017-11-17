package com.xiao.wx_order.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lly835.bestpay.model.RefundResponse;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.service.OrderService;
import com.xiao.wx_order.service.PayService;
import com.xiao.wx_order.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {
	@Autowired
	private PayService payService;

	@Autowired
	private OrderService orderService;

	@Test
	public void create() {
		OrderDTO orderDTO = orderService.findOne("1508943088595690209");
		payService.create(orderDTO);
	}

	@Test
	public void refund() {
		OrderDTO orderDTO = orderService.findOne("1510635166005385287");
		RefundResponse refundResponse = payService.refund(orderDTO);
		log.info(JsonUtil.toJson(refundResponse));
	}

}
