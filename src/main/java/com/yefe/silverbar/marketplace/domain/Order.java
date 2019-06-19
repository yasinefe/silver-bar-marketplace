package com.yefe.silverbar.marketplace.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;

@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@Getter
public class Order {

    private final String id;
    private final String userId;
    private final BigDecimal quantity;
    private final BigDecimal price;
    private final OrderType type;

    public static Order createOrder(String userId, double quantity, double price, OrderType tyoe) {
        return new Order(randomUUID().toString(), userId, valueOf(quantity), valueOf(price), tyoe);
    }

    public boolean isType(OrderType orderType) {
        return type.equals(orderType);
    }

}
