package com.xiao.wx_order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xiao.wx_order.dto.OrderDTO;

public interface OrderService {
	/**
	 * 创建订单
	 */
	OrderDTO create(OrderDTO orderDTO);

	/**
	 * 查询单个订单
	 */
	OrderDTO findOne(String orderId);

	/**
	 * 查询订单列表
	 */

	Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

	Page<OrderDTO> findList(Pageable pageable);

	/**
	 * 取消订单
	 */
	OrderDTO cancle(OrderDTO orderDTO);

	/**
	 * 完结订单
	 */
	OrderDTO finish(OrderDTO orderDTO);

	/**
	 * 支付订单
	 */
	OrderDTO paid(OrderDTO orderDTO);
}
