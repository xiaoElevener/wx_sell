package com.xiao.wx_order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class SellerInfo {

	@Id
	private String sellerId;

	private String username;

	private String password;

	private String openid;

}
