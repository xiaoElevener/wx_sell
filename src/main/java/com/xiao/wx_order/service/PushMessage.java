package com.xiao.wx_order.service;

import com.xiao.wx_order.dto.OrderDTO;

/**
 * 推送消息
 * 
 * @author Administrator
 *
 */
public interface PushMessage {
	/**
	 * 订单状态变更消息
	 * 
	 * @param orderDTO
	 */
	void orderStatus(OrderDTO orderDTO);
}
