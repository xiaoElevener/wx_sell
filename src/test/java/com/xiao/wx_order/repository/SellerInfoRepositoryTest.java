package com.xiao.wx_order.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.SellerInfo;
import com.xiao.wx_order.utils.KeyUtil;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoRepositoryTest {

	@Autowired
	private SellerInfoRepository repository;

	@Test
	public void save() {
		SellerInfo sellerInfo = new SellerInfo();
		sellerInfo.setSellerId(KeyUtil.getUniqueKey());
		sellerInfo.setUsername("admin");
		sellerInfo.setPassword("admin");
		sellerInfo.setOpenid("abc");

		SellerInfo result = repository.save(sellerInfo);
		Assert.assertNotNull(result);
	}

	@Test
	public void findByOpenid() {
		SellerInfo result = repository.findByOpenid("abc");
		Assert.assertEquals("abc", result.getOpenid());
	}
	
	@Test
	public void findByUsernameAndPassowrd(){
		String username="admin";
		String password="admin";
		SellerInfo result=repository.findByUsernameAndPassword(username, password);
		Assert.assertNotNull(result);
	}

}
