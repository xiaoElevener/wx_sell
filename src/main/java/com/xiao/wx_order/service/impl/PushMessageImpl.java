package com.xiao.wx_order.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiao.wx_order.config.WechatAccountConfig;
import com.xiao.wx_order.dto.OrderDTO;
import com.xiao.wx_order.service.PushMessage;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

@Service
@Slf4j
public class PushMessageImpl implements PushMessage {

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private WechatAccountConfig accountConfig;

	/**
	 * 模版消息推送 需要微信公众平台 白名单ip地址
	 */
	@Override
	public void orderStatus(OrderDTO orderDTO) {
		WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
		wxMpTemplateMessage.setTemplateId(accountConfig.getTemplateId().get("orderStatus"));
		wxMpTemplateMessage.setUrl(orderDTO.getBuyerOpenid());
		List<WxMpTemplateData> data = Arrays.asList(new WxMpTemplateData("first", "亲，记得收获"),
				new WxMpTemplateData("keyword1", "微信点餐"), new WxMpTemplateData("keyword2", "123123"),
				new WxMpTemplateData("keyword3", orderDTO.getOrderId()),
				new WxMpTemplateData("keyword4", orderDTO.getOrderStatusEnum().message),
				new WxMpTemplateData("keyword5", orderDTO.getOrderAmount().toString()));
		wxMpTemplateMessage.setData(data);
		try {
			wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
		} catch (WxErrorException e) {
			log.error("【微信模版消息】发送失败,{}", e.getMessage());
		}

	}

}
