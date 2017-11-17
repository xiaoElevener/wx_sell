package com.xiao.wx_order.controller;

import java.net.URLDecoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lly835.bestpay.model.PayResponse;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.service.OrderService;
import com.xiao.wx_order.service.PayService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PayController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PayService payService;

	@GetMapping("/pay")
	public ModelAndView pay(@RequestParam("orderId") String orderId, @RequestParam("returnUrl") String returnUrl,
			Map<String, Object> map) {
		log.info("【创建支付订单】订单号={},returnUrl={}", orderId, returnUrl);
		// 1.查询订单
		OrderDTO orderDTO = orderService.findOne(orderId);
		if (orderDTO == null) {
			log.error("【创建支付订单】订单号错误，orderDTO={}", orderDTO);
			throw new SellException(ResultEnum.ORDER_NOT_EXIST);
		}

		// 2. 发起支付
		PayResponse payResponse = payService.create(orderDTO);
		map.put("payResponse", payResponse);
		map.put("returnUrl", URLDecoder.decode(returnUrl));
		log.info("【创建支付订单】跳转地址={}", URLDecoder.decode(returnUrl));
		return new ModelAndView("pay/create");
	};

	/**
	 * 
	 * @param notifyData
	 *            (xml格式的字符串)
	 * @return
	 */
	@PostMapping("/pay/notify")
	public ModelAndView notify(@RequestBody String notifyData) {
		payService.notify(notifyData);
		return new ModelAndView("pay/success");
	}

}
