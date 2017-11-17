package com.xiao.wx_order.service;

import java.util.List;

import com.xiao.wx_order.entity.ProductCategory;

public interface CategoryService {
	ProductCategory findOne(Integer categoryId);

	List<ProductCategory> findAll();

	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

	ProductCategory save(ProductCategory productCategory);
}
