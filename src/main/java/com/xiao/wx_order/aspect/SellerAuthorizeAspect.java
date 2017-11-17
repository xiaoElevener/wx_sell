package com.xiao.wx_order.aspect;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xiao.wx_order.constant.CookieConstant;
import com.xiao.wx_order.constant.RedisConstant;
import com.xiao.wx_order.exception.SellerAuthorizeExecption;
import com.xiao.wx_order.utils.CookieUtil;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Pointcut("execution(public * com.xiao.wx_order.controller.Seller*.*(..))"
			+ "&& !execution(public * com.xiao.wx_order.controller.SellerUserController.*(..))")
	public void verify() {

	}

	@Before("verify()")
	public void doVerify() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 查询cookie
		Cookie cookie = CookieUtil.get(request, CookieConstant.TOEKN);
		if (cookie == null) {
			log.warn("【登录校验】cookie中查不到token");
			throw new SellerAuthorizeExecption();
		}

		// 去redis里查询
		String tokenValue = redisTemplate.opsForValue()
				.get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

		if (StringUtils.isEmpty(tokenValue)) {
			log.warn("【登录校验】redis中查不到token");
			throw new SellerAuthorizeExecption();
		}
	}

}
