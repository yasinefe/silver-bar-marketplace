package com.yefe.silverbar.marketplace.repository;

import com.yefe.silverbar.marketplace.domain.Order;
import com.yefe.silverbar.marketplace.domain.OrderType;

import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

public class InMemoryOrderRepository implements OrderRepository {

    private Map<String, Order> orders = new HashMap<>();

    @Override
    public Order insert(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> delete(String orderId) {
        Order removedOrder = orders.remove(orderId);
        return removedOrder != null ? of(removedOrder) : empty();
    }

    @Override
    public List<Order> getOrdersByType(OrderType orderType) {
        return orders.values().stream()
                .filter(order -> order.isType(orderType))
                .collect(toList());
    }
}
