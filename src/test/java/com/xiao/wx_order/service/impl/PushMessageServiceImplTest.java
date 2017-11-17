package com.xiao.wx_order.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.config.WechatAccountConfig;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushMessageServiceImplTest {

	@Autowired
	private PushMessageImpl pushMessageService;

	@Autowired
	private OrderService orderService;
	
	
	@Test
	public void orderStatus() throws Exception {
		OrderDTO orderDTO = orderService.findOne("1510670948055837082");
		pushMessageService.orderStatus(orderDTO);
	}

}
