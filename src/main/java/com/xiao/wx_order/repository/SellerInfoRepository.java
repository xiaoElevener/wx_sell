package com.xiao.wx_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiao.wx_order.entity.SellerInfo;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
	SellerInfo findByOpenid(String openid);

	SellerInfo findByUsernameAndPassword(String username, String password);
}
