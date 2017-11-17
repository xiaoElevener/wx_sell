package com.xiao.wx_order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiao.wx_order.VO.ProductInfoVO;
import com.xiao.wx_order.VO.ProductVO;
import com.xiao.wx_order.VO.ResultVO;
import com.xiao.wx_order.entity.ProductCategory;
import com.xiao.wx_order.entity.ProductInfo;
import com.xiao.wx_order.service.CategoryService;
import com.xiao.wx_order.service.ProductService;
import com.xiao.wx_order.utils.ResultVOUtil;

/**
 * 买家商品
 */

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/list")
	//@Cacheable(cacheNames="product",key="123")
	public ResultVO list() {
		// 1.查询所有的上架商品
		List<ProductInfo> productInfoList = productService.findUpAll();

		// 2.查询类目（一次性查询）

		// 精简方法(java8,lambda)
		List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType())
				.collect(Collectors.toList());
		List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

		// 3.数据拼装
		List<ProductVO> productVOList = new ArrayList<>();

		for (ProductCategory productCategory : productCategoryList) {
			ProductVO productVO = new ProductVO();
			productVO.setCategoryType(productCategory.getCategoryType());
			productVO.setCategoryName(productCategory.getCategoryName());

			List<ProductInfoVO> productInfoVOList = new ArrayList<>();
			for (ProductInfo productInfo : productInfoList) {
				if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
					ProductInfoVO productInfoVO = new ProductInfoVO();
					BeanUtils.copyProperties(productInfo, productInfoVO);
					productInfoVOList.add(productInfoVO);
				}
			}
			productVO.setProductInfoVOList(productInfoVOList);
			productVOList.add(productVO);
		}

		return ResultVOUtil.success(productVOList);
	}

}
