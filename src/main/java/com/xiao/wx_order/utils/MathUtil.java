package com.xiao.wx_order.utils;

import org.mockito.internal.matchers.Equals;

public class MathUtil {

	private static final Double MONEY_RANGE = 0.01;

	/**
	 * 比较2个金额是否相等
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Boolean equals(Double d1, Double d2) {
		if (Math.abs(d1 - d2) < MONEY_RANGE)
			return true;
		else {
			return false;
		}
	}
}
