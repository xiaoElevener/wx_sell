package com.xiao.wx_order.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.SellerInfo;
import com.xiao.wx_order.service.SellerService;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerServiceImplTest {

	private final String OPENID = "abc";

	@Autowired
	private SellerService sellerService;

	@Test
	public void find() {
		SellerInfo result = sellerService.findSellerInfoByOpenid(OPENID);
		Assert.assertEquals(OPENID, result.getOpenid());
	}

}
