package com.yefe.silverbar.marketplace.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class OrderSummary {

    private static final String ORDER_SUMMARY_LINE = "%s kg for £%s";

    private final BigDecimal totalQuantity;
    private final BigDecimal price;

    public static OrderSummary createOrderSummary(BigDecimal price, BigDecimal totalQuantity) {
        return new OrderSummary(price, totalQuantity);
    }

    @Override
    public String toString() {
        return String.format(ORDER_SUMMARY_LINE, totalQuantity.toString(), price.toBigInteger());
    }
}
