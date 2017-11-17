package com.xiao.wx_order.service;
/**
 * 买家
 */

import com.xiao.wx_order.dto.OrderDTO;

public interface BuyerService {

	// 查询一个订单
	public OrderDTO findOrderOne(String openid, String orderId);
	
	public OrderDTO cancelOrder(String openid, String orderId);
}
