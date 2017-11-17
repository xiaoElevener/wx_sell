package com.xiao.wx_order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.xiao.wx_order.enums.OrderStatusEnum;
import com.xiao.wx_order.enums.PayStatusEnum;

import lombok.Data;

/**
 * 订单主表
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

	/**
	 * 订单id
	 */
	@Id
	private String orderId;

	/**
	 * 买家姓名
	 */
	private String buyerName;

	/**
	 * 买家手机号
	 */
	private String buyerPhone;

	/**
	 * 买家地址
	 */
	private String buyerAddress;

	/**
	 * 买家微信Openid
	 */
	private String buyerOpenid;

	/**
	 * 订单总金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 订单状态,默认为新下单0
	 */
	private Integer orderStatus = OrderStatusEnum.NEW.code;

	/**
	 * 支付状态，0为未支付
	 */
	private Integer payStatus = PayStatusEnum.WAIT.code;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
}
