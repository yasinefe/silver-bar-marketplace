package com.yefe.silverbar.marketplace;

import com.yefe.silverbar.marketplace.domain.OrderSummary;
import com.yefe.silverbar.marketplace.exception.InvalidParameterException;
import com.yefe.silverbar.marketplace.exception.OrderNotFoundException;
import com.yefe.silverbar.marketplace.renderer.LineListOrderSummaryRenderer;
import com.yefe.silverbar.marketplace.renderer.OrderSummaryRenderer;
import com.yefe.silverbar.marketplace.domain.Order;
import com.yefe.silverbar.marketplace.domain.OrderType;
import com.yefe.silverbar.marketplace.repository.InMemoryOrderRepository;
import com.yefe.silverbar.marketplace.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.yefe.silverbar.marketplace.domain.OrderSummary.createOrderSummary;
import static com.yefe.silverbar.marketplace.domain.OrderType.SELL;
import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;

public class LiveOrderBoard<OUTPUT> {

    private static final Comparator<OrderSummary> PRICE_COMPARATOR = comparing(OrderSummary::getPrice);

    private OrderSummaryRenderer<OUTPUT> orderSummaryRenderer;

    private OrderRepository orderRepository = new InMemoryOrderRepository();

    public static LiveOrderBoard<String> createWithLineListOrderSummaryRenderer() {
        return new LiveOrderBoard<>(new LineListOrderSummaryRenderer());
    }

    public static <T> LiveOrderBoard<T> createWithOrderSummaryRenderer(OrderSummaryRenderer<T> orderSummaryRenderer) {
        return new LiveOrderBoard<>(orderSummaryRenderer);
    }

    private LiveOrderBoard(OrderSummaryRenderer<OUTPUT> orderSummaryRenderer) {
        this.orderSummaryRenderer = ofNullable(orderSummaryRenderer)
                .orElseThrow(() -> new InvalidParameterException("Order summary renderer can not be null"));
    }

    public LiveOrderBoard withOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = ofNullable(orderRepository)
                .orElseThrow(() -> new InvalidParameterException("Order repository can not be null"));
        return this;
    }

    public Order register(Order order) {
        return orderRepository.insert(order);
    }

    public Order cancel(String orderId) {
        return orderRepository.delete(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public OUTPUT summary(OrderType orderType) {
        return orderSummaryRenderer.render(
                calculate(orderRepository.getOrdersByType(orderType))
                        .sorted(orderType == SELL ? PRICE_COMPARATOR : PRICE_COMPARATOR.reversed())
                        .collect(toList())
        );
    }

    private Stream<OrderSummary> calculate(List<Order> orders) {
        Map<BigDecimal, BigDecimal> priceQuantityMap = orders.stream()
                .collect(
                        groupingBy(Order::getPrice,
                                mapping(Order::getQuantity, reducing(BigDecimal.ZERO, BigDecimal::add))
                        )
                );

        return priceQuantityMap.entrySet().stream()
                .map(entry -> createOrderSummary(entry.getValue(), entry.getKey()));
    }

}
