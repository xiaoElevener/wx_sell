package com.xiao.wx_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WxSellApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxSellApplication.class, args);
	}
}
