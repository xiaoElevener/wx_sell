package com.xiao.wx_order.controller;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.xiao.wx_order.constant.CookieConstant;
import com.xiao.wx_order.constant.RedisConstant;
import com.xiao.wx_order.entity.SellerInfo;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.service.SellerService;
import com.xiao.wx_order.utils.CookieUtil;

/**
 * 卖家用户
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

	@Autowired
	private SellerService sellerService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@PostMapping("/login")
	public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletResponse response, Map<String, Object> map) {
		// 1.用户名密码去和数据库里的数据匹配

		SellerInfo sellerInfo = sellerService.findSellerInfoByUsernameAndPassword(username, password);
		if (sellerInfo == null) {
			map.put("msg", ResultEnum.LOGIN_FAIL.message);
			map.put("url", "/seller/order/list");
			return new ModelAndView("common/error");
		}
		// 2.设置token至redis
		String token = UUID.randomUUID().toString();
		Integer expire = RedisConstant.EXPIRE;
		redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), username, expire,
				TimeUnit.SECONDS);

		// 3.设置token至cookie
		CookieUtil.set(response, CookieConstant.TOEKN, token, expire);
		map.put("url", "/seller/order/list");
		return new ModelAndView("common/success", map);
	}

	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
		// 1.从cookie里查询
		Cookie cookie = CookieUtil.get(request, CookieConstant.TOEKN);
		if (cookie != null) {
			// 2.清除redis
			redisTemplate.opsForValue().getOperations()
					.delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
		}

		// 3.清除cookie
		CookieUtil.set(response, CookieConstant.TOEKN, null, 0);

		map.put("msg", ResultEnum.LOGOUT_SUCCESS.message);
		map.put("url", "/seller/order/list");
		return new ModelAndView("common/success", map);
	}

	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("seller/login");
	}

}
