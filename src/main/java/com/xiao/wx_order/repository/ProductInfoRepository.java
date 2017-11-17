package com.xiao.wx_order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiao.wx_order.entity.ProductInfo;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
	List<ProductInfo> findByProductStatus(Integer productStatus);
}
