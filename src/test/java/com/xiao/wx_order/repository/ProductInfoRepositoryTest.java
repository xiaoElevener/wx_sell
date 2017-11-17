package com.xiao.wx_order.repository;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiao.wx_order.entity.ProductInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
	@Autowired
	private ProductInfoRepository repository;

	@Test
	public void saveTest() {
		ProductInfo productInfo = new ProductInfo("123456", "皮蛋粥", new BigDecimal(10.5), 10, "很好喝的粥", "pdz.jpg", 0, 2);
		ProductInfo result = repository.save(productInfo);
		Assert.assertNotNull(result);
	}

	@Test
	public void findByProductStatus() {
		List<ProductInfo> productInfoList = repository.findByProductStatus(0);
		Assert.assertNotEquals(0, productInfoList.size());
	}

}
