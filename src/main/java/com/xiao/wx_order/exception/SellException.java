package com.xiao.wx_order.exception;

import com.xiao.wx_order.enums.ResultEnum;

import lombok.Getter;

@Getter
public class SellException extends RuntimeException {
	private Integer code;

	public SellException(ResultEnum resultEnum) {
		super(resultEnum.message);
		this.code = resultEnum.code;
	}

	public SellException(Integer code, String message) {
		super(message);
		this.code = code;
	}

}
