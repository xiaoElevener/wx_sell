package com.xiao.wx_order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiao.wx_order.entity.SellerInfo;
import com.xiao.wx_order.repository.SellerInfoRepository;
import com.xiao.wx_order.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	private SellerInfoRepository repository;

	@Override
	public SellerInfo findSellerInfoByOpenid(String openid) {
		return repository.findByOpenid(openid);
	}

	@Override
	public SellerInfo findSellerInfoByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

}
