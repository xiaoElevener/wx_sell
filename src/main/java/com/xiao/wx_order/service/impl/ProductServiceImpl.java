package com.xiao.wx_order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiao.wx_order.dto.CartDTO;
import com.xiao.wx_order.entity.ProductInfo;
import com.xiao.wx_order.enums.ProductStatusEnum;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.repository.ProductInfoRepository;
import com.xiao.wx_order.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductInfoRepository repository;

	@Override
	@Cacheable(cacheNames = "product", key = "#productId")
	public ProductInfo findOne(String productId) {
		return repository.findOne(productId);
	}

	@Override
	public List<ProductInfo> findUpAll() {
		return repository.findByProductStatus(ProductStatusEnum.UP.code);
	}

	@Override
	public Page<ProductInfo> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	@CachePut(cacheNames = "product", key = "#productInfo.getProductId()")
	public ProductInfo save(ProductInfo productInfo) {
		return repository.save(productInfo);
	}

	@Override
	@Transactional
	public void increaseStock(List<CartDTO> cartDTOList) {
		for (CartDTO cartDTO : cartDTOList) {
			ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
			if (productInfo == null) {
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			productInfo.setProductStock(productInfo.getProductStock() + cartDTO.getProductQuantity());
			repository.save(productInfo);
		}
	}

	@Override
	@Transactional
	public void decreaseStock(List<CartDTO> cartDTOList) {
		for (CartDTO cartDTO : cartDTOList) {
			ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
			if (productInfo == null) {
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
			if (result < 0) {
				throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
			}
			productInfo.setProductStock(result);
		}
	}

	@Override
	public ProductInfo onSale(String productId) {
		ProductInfo productInfo = repository.findOne(productId);
		if (productInfo == null) {
			throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
		}
		if (productInfo.getProductStatusEnum() == ProductStatusEnum.UP) {
			throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
		}

		productInfo.setProductStatus(ProductStatusEnum.UP.code);
		return repository.save(productInfo);
	}

	@Override
	public ProductInfo offSale(String productId) {
		ProductInfo productInfo = repository.findOne(productId);
		if (productInfo == null) {
			throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
		}
		if (productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN) {
			throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
		}

		productInfo.setProductStatus(ProductStatusEnum.DOWN.code);
		return repository.save(productInfo);
	}

}
