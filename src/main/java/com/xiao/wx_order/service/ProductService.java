package com.xiao.wx_order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xiao.wx_order.dto.CartDTO;
import com.xiao.wx_order.entity.ProductInfo;

/**
 * 商品
 */
public interface ProductService {
	ProductInfo findOne(String productId);

	/**
	 * 查询所有在架商品列表
	 * 
	 * @return
	 */
	List<ProductInfo> findUpAll();

	Page<ProductInfo> findAll(Pageable pageable);

	ProductInfo save(ProductInfo productInfo);

	// 加库存
	void increaseStock(List<CartDTO> cartDTOList);

	// 减库存
	void decreaseStock(List<CartDTO> cartDTOList);
	
	
	//上架
	ProductInfo onSale(String productId);
	
	//下架
	ProductInfo offSale(String productId);
}
