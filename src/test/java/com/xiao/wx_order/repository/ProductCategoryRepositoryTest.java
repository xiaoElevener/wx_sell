package com.xiao.wx_order.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.xiao.wx_order.entity.ProductCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
	@Autowired
	private ProductCategoryRepository repository;

	@Test
	public void findOneTest() {
		ProductCategory productCategory = repository.findOne(1);
		System.out.println(productCategory);
	}

	@Test
	@Transactional
	public void saveTest() {
		ProductCategory productCategory = new ProductCategory("我的最爱", 8);
		ProductCategory result = repository.save(productCategory);
		Assert.assertNotNull(result);
	}

	@Test
	public void UpdateTest() {
		ProductCategory productCategory = repository.findOne(2);
		productCategory.setCategoryName("到底谁最爱");
		repository.save(productCategory);
	}

	@Test
	public void findByCategoryTypeInTest() {
		List<Integer> list = Arrays.asList(2, 3, 6);
		List<ProductCategory> result = repository.findByCategoryTypeIn(list);
		Assert.assertNotEquals(0, result.size());
	}

}
