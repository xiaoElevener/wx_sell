package com.xiao.wx_order.repository;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.OrderDetail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

	@Autowired
	private OrderDetailRepository repository;

	@Test
	public void saveTest() {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setDetailId("123322");
		orderDetail.setOrderId("123456");
		orderDetail.setProductIcon("prd.jpg");
		orderDetail.setProductId("32154");
		orderDetail.setProductName("鸡爪");
		orderDetail.setProductPrice(new BigDecimal(21.6));
		orderDetail.setProductQuantity(10);

		OrderDetail result = repository.save(orderDetail);
		Assert.assertNotNull(result);
	}

	@Test
	public void findByOrderIdTest() {
		List<OrderDetail> result = repository.findByOrderId("123456");
		Assert.assertNotEquals(0, result.size());
	}

}
