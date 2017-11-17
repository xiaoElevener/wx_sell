package com.xiao.wx_order.service;

import com.xiao.wx_order.entity.SellerInfo;

public interface SellerService {

	SellerInfo findSellerInfoByOpenid(String openid);

	SellerInfo findSellerInfoByUsernameAndPassword(String username,String password);

}
