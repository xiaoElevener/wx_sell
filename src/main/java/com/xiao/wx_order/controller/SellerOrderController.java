package com.xiao.wx_order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.enums.ResultEnum;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/list")
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size, Map<String, Object> map) {
		PageRequest request = new PageRequest(page - 1, size);
		Page<OrderDTO> orderDTOPage = orderService.findList(request);
		map.put("orderDTOPage", orderDTOPage);
		map.put("currentPage", page);
		map.put("size", size);
		return new ModelAndView("order/list", map);
	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	@GetMapping("/cancel")
	public ModelAndView cancel(@RequestParam("orderId") String orderId, Map<String, Object> map) {
		try {
			OrderDTO orderDTO = orderService.findOne(orderId);
			orderService.cancle(orderDTO);
		} catch (SellException e) {
			log.error("【卖家端取消订单】发生异常{}", e.getMessage());
			map.put("msg", e.getMessage());
			map.put("url", "/seller/order/list");
			return new ModelAndView("common/error", map);
		}
		map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.message);
		map.put("url", "/seller/order/list");
		return new ModelAndView("common/success");
	}

	/**
	 * 订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@GetMapping("/detail")
	public ModelAndView detail(@RequestParam("orderId") String orderId, Map<String, Object> map) {
		OrderDTO orderDTO = null;
		try {
			orderDTO = orderService.findOne(orderId);
		} catch (SellException e) {
			log.error("【卖家端查询订单】 发生异常{}", e.getMessage());
			map.put("msg", e.getMessage());
			map.put("url", "/seller/order/list");
			return new ModelAndView("common/error", map);
		}

		map.put("orderDTO", orderDTO);
		return new ModelAndView("order/detail", map);
	}
	
	/**
	 * 完结订单
	 * @param orderId
	 * @param map
	 * @return
	 */
	@GetMapping("/finish")
	public ModelAndView finish(@RequestParam("orderId") String orderId, Map<String, Object> map){
		try {
			OrderDTO orderDTO = orderService.findOne(orderId);
			orderService.finish(orderDTO);
		} catch (SellException e) {
			log.error("【卖家端完结订单】发生异常{}", e.getMessage());
			map.put("msg", e.getMessage());
			map.put("url", "/seller/order/list");
			return new ModelAndView("common/error", map);
		}
		map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.message);
		map.put("url", "/seller/order/list");
		return new ModelAndView("common/success");
	}
}
