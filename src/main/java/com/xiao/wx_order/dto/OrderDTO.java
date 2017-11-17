package com.xiao.wx_order.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xiao.wx_order.entity.OrderDetail;
import com.xiao.wx_order.enums.OrderStatusEnum;
import com.xiao.wx_order.enums.PayStatusEnum;
import com.xiao.wx_order.utils.EnumUtil;
import com.xiao.wx_order.utils.serializer.Date2LongSerializer;

import lombok.Data;

@Data
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

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
	private Integer orderStatus;

	/**
	 * 支付状态，0为未支付
	 */
	private Integer payStatus;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = Date2LongSerializer.class)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = Date2LongSerializer.class)
	private Date updateTime;

	List<OrderDetail> orderDetailList;

	@JsonIgnore
	public OrderStatusEnum getOrderStatusEnum() {
		return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
	}
	
	@JsonIgnore
	public PayStatusEnum getPayStatusEnum() {
		return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
	}

}
