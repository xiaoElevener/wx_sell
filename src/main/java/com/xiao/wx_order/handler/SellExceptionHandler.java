package com.xiao.wx_order.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xiao.wx_order.VO.ResultVO;
import com.xiao.wx_order.config.ProjectUrlConfig;
import com.xiao.wx_order.exception.SellException;
import com.xiao.wx_order.exception.SellerAuthorizeExecption;
import com.xiao.wx_order.utils.ResultVOUtil;

@ControllerAdvice
public class SellExceptionHandler {

	@Autowired
	private ProjectUrlConfig projectUrlConfig;

	// 拦截登录异常
	@ExceptionHandler(value = SellerAuthorizeExecption.class)
	public ModelAndView handleAuthorizeException() {
		return new ModelAndView("/seller/login");
	}

	@ExceptionHandler(value = SellException.class)
	@ResponseBody
	public ResultVO handlerSellExecption(SellException e) {
		return ResultVOUtil.error(e.getCode(), e.getMessage());
	}

}
