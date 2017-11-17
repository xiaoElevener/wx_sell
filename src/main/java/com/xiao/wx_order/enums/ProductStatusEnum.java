package com.xiao.wx_order.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum implements CodeEnum {

	UP(0, "上架"), DOWN(1, "下架");

	final public Integer code;

	final public String message;

	private ProductStatusEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

}
