package com.xiao.wx_order.constant;

/**
 * redis常量
 */
public interface RedisConstant {

	String TOKEN_PREFIX = "token_%s"; //前缀

	Integer EXPIRE = 7200; // 2小时
}
