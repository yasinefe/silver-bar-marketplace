package com.yefe.silverbar.marketplace.renderer;

import com.yefe.silverbar.marketplace.domain.OrderSummary;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class LineListOrderSummaryRenderer implements OrderSummaryRenderer<String> {

    @Override
    public String render(List<OrderSummary> orderSummaries) {
        return orderSummaries.stream()
                .map(OrderSummary::toString)
                .collect(joining("\n"));
    }

}
