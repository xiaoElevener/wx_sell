package com.xiao.wx_order.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum {

	WAIT(0, "等待支付"), SUCCESS(1, "支付成功");

	final public Integer code;
	final public String message;

	private PayStatusEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
