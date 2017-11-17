package com.xiao.wx_order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.xiao.wx_order.enums.ProductStatusEnum;
import com.xiao.wx_order.utils.EnumUtil;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

/**
 * 商品
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo implements Serializable {

	private static final long serialVersionUID = 7047380488402030504L;

	@Id
	private String productId;

	/**
	 * 名字
	 */
	private String productName;

	/**
	 * 单价
	 */
	private BigDecimal productPrice;

	/**
	 * 库存
	 */
	private Integer productStock;

	/**
	 * 描述
	 */
	private String productDescription;

	/**
	 * 小图
	 */
	private String productIcon;

	/**
	 * 状态，0正常，1下架
	 */
	private Integer productStatus = ProductStatusEnum.UP.getCode();

	/**
	 * 类目编号
	 */
	private Integer categoryType;

	/**
	 * 修改时间
	 */
	private Date createTime;

	/**
	 * 创建时间
	 */
	private Date updateTime;

	@Ignore
	public ProductStatusEnum getProductStatusEnum() {
		return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
	};

	public ProductInfo() {
		super();
	}

	public ProductInfo(String productId, String productName, BigDecimal productPrice, Integer productStock,
			String productDescription, String productIcon, Integer productStatus, Integer categoryType) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productStock = productStock;
		this.productDescription = productDescription;
		this.productIcon = productIcon;
		this.productStatus = productStatus;
		this.categoryType = categoryType;
	}

}
