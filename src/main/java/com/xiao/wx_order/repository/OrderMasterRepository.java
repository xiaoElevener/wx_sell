package com.xiao.wx_order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xiao.wx_order.entity.OrderMaster;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
	Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
