package com.xiao.wx_order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiao.wx_order.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
	List<OrderDetail> findByOrderId(String OrderId);
}
