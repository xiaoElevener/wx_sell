package com.xiao.wx_order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiao.wx_order.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
