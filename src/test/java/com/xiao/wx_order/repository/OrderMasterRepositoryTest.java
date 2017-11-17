package com.xiao.wx_order.repository;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.OrderMaster;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

	@Autowired
	private OrderMasterRepository repository;

	private final String OPENID = "110110";

	@Test
	public void saveTest() {
		OrderMaster orderMaster = new OrderMaster();
		orderMaster.setOrderId("123457");
		orderMaster.setBuyerName("萧萧");
		orderMaster.setBuyerPhone("15797692522");
		orderMaster.setBuyerAddress("中国福建");
		orderMaster.setBuyerOpenid("110110");
		orderMaster.setOrderAmount(new BigDecimal(20.4));

		OrderMaster result = repository.save(orderMaster);
		Assert.assertNotEquals(null, result);

	}

	@Test
	public void findByBuyerOpenidTest() {
		PageRequest request = new PageRequest(0, 3);

		Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, request);
		
		Assert.assertNotEquals(0, result.getTotalElements());
		
	}
}
