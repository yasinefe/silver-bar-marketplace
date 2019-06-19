package com.yefe.silverbar.marketplace.renderer;

import com.yefe.silverbar.marketplace.domain.OrderSummary;

import java.util.List;

public interface OrderSummaryRenderer<OUT> {

    OUT render(List<OrderSummary> orderSummaries);

}
