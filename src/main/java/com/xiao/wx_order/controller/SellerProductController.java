package com.xiao.wx_order.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.xiao.wx_order.entity.ProductCategory;
import com.xiao.wx_order.entity.ProductInfo;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.form.ProductForm;
import com.xiao.wx_order.service.CategoryService;
import com.xiao.wx_order.service.ProductService;
import com.xiao.wx_order.utils.KeyUtil;

/**
 * 卖家端商品
 * 
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * 列表
	 * 
	 * @param page
	 * @param size
	 * @param map
	 * @return
	 */
	@GetMapping("/list")
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size, Map<String, Object> map) {
		PageRequest request = new PageRequest(page - 1, size);
		Page<ProductInfo> productInfoPage = productService.findAll(request);
		map.put("productInfoPage", productInfoPage);
		map.put("currentPage", page);
		map.put("size", size);
		return new ModelAndView("product/list", map);
	}

	/**
	 * 商品上架
	 * 
	 * @param productId
	 * @param map
	 * @return
	 */
	@GetMapping("/onSale")
	public ModelAndView onSale(@RequestParam("productId") String productId, Map<String, Object> map) {
		try {
			productService.onSale(productId);
		} catch (SellException e) {
			map.put("msg", e.getMessage());
			map.put("url", "/seller/product/list");
			return new ModelAndView("common/error", map);
		}
		map.put("url", "/seller/product/list");
		return new ModelAndView("common/success", map);
	}

	/**
	 * 商品下架
	 * 
	 * @param productId
	 * @param map
	 * @return
	 */
	@GetMapping("/offSale")
	public ModelAndView offSale(@RequestParam("productId") String productId, Map<String, Object> map) {
		try {
			productService.offSale(productId);
		} catch (Exception e) {
			map.put("msg", e.getMessage());
			map.put("url", "/seller/product/list");
			return new ModelAndView("common/error", map);
		}
		map.put("url", "/seller/product/list");
		return new ModelAndView("common/success", map);
	}

	@GetMapping("/index")
	public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
			Map<String, Object> map) {
		if (!StringUtils.isEmpty(productId)) {
			ProductInfo productInfo = productService.findOne(productId);
			map.put("productInfo", productInfo);
		}

		List<ProductCategory> categoryList = categoryService.findAll();
		map.put("categoryList", categoryList);
		return new ModelAndView("product/index", map);
	}

	@PostMapping("/save")
	//@CacheEvict(cacheNames="product",key="123")
	public ModelAndView save(@Valid ProductForm productForm, BindingResult bindingResult, Map<String, Object> map) {

		if (bindingResult.hasErrors()) {
			map.put("msg", bindingResult.getFieldError().getDefaultMessage());
			map.put("url", "/seller/product/list");
			new ModelAndView("common/error", map);
		}
		try {
			ProductInfo productInfo = new ProductInfo();
			if (!StringUtils.isEmpty(productForm.getProductId())) {
				productInfo = productService.findOne(productForm.getProductId());
			} else {
				productForm.setProductId(KeyUtil.getUniqueKey());
			}
			BeanUtils.copyProperties(productForm, productInfo);
			productService.save(productInfo);
		} catch (SellException e) {
			map.put("msg", e.getMessage());
			map.put("url", "/seller/product/index");
			return new ModelAndView("common/error", map);
		}
		map.put("url", "/seller/product/list");
		return new ModelAndView("common/success", map);
	}
}
