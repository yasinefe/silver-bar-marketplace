package com.yefe.silverbar.marketplace.repository;

import com.yefe.silverbar.marketplace.domain.Order;
import com.yefe.silverbar.marketplace.domain.OrderType;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order insert(Order order);

    Optional<Order> delete(String orderId);

    List<Order> getOrdersByType(OrderType orderType);

}
