package com.xiao.wx_order.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum{
	NEW(0, "新订单"), FINISHED(1, "完结"), CANCEL(2, "已取消");

	final public Integer code;

	final public String message;

	private OrderStatusEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
