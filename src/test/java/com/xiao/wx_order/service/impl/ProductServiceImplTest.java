package com.xiao.wx_order.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.ProductInfo;
import com.xiao.wx_order.enums.ProductStatusEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

	@Autowired
	private ProductServiceImpl productService;

	@Test
	public void findOneTest() {
		ProductInfo productInfo = productService.findOne("123456");
		Assert.assertEquals("123456", productInfo.getProductId());
	};

	@Test
	public void findUpAllTest() {
		List<ProductInfo> productInfoList = productService.findUpAll();
		Assert.assertNotEquals(0, productInfoList.size());
	};

	@Test
	public void findAllTest() {
		Pageable request = new PageRequest(0, 2);
		Page<ProductInfo> page = productService.findAll(request);
		System.out.println(page.getTotalElements());
	};

	@Test
	public void saveTest() {
		ProductInfo productInfo = new ProductInfo("654321", "猪头肉", new BigDecimal(20.4), 20, "很好吃的肉", "ztr.jpg", 1, 2);
		ProductInfo result = productService.save(productInfo);
		Assert.assertEquals("654321", result.getProductId());
	};

	@Test
	public void onSell() {
		ProductInfo result = productService.onSale("1");
		Assert.assertEquals(ProductStatusEnum.UP.code, result.getProductStatus());
	}
	
	@Test
	public void offSell() {
		ProductInfo result = productService.offSale("1");
		Assert.assertEquals(ProductStatusEnum.DOWN.code, result.getProductStatus());
	}
}
