package com.xiao.wx_order.VO;

import java.io.Serializable;

import lombok.Data;

/**
 * http请求的最外层对象
 */
@Data
public class ResultVO<T> implements Serializable{
	
	private static final long serialVersionUID = 6125018797726033739L;

	/**
	 * 错误码
	 */
	private Integer code;

	/**
	 * 提示信息
	 */
	private String msg;

	/**
	 * 具体内容
	 */
	private T data;
}
