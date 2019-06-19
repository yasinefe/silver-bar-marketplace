package com.yefe.silverbar.marketplace;

import com.yefe.silverbar.marketplace.domain.Order;
import com.yefe.silverbar.marketplace.domain.OrderSummary;
import com.yefe.silverbar.marketplace.exception.InvalidParameterException;
import com.yefe.silverbar.marketplace.exception.OrderNotFoundException;
import com.yefe.silverbar.marketplace.renderer.OrderSummaryRenderer;
import com.yefe.silverbar.marketplace.repository.OrderRepository;
import org.junit.Test;

import java.util.List;

import static com.yefe.silverbar.marketplace.domain.Order.createOrder;
import static com.yefe.silverbar.marketplace.domain.OrderType.BUY;
import static com.yefe.silverbar.marketplace.domain.OrderType.SELL;
import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class LiveOrderBoardTest {

    private Order sellOrder = createOrder("a_user_id", 3.5, 306, SELL);
    private Order anotherSellOrderWithSamePrice = createOrder("a_user_id", 1.5, 306, SELL);
    private Order sellOrderWithGreaterPrice = createOrder("another_user_id", 1.2, 310, SELL);

    private Order buyOrder = createOrder("a_user_id", 3.5, 306, BUY);
    private Order buyOrderWithGreaterPrice = createOrder("a_user_id", 1.2, 310, BUY);

    private OrderSummaryRenderer<List<OrderSummary>> mockOrderSummaryRenderer = mock(OrderSummaryRenderer.class);
    private OrderRepository mockOrderRepository = mock(OrderRepository.class);

    private LiveOrderBoard<List<OrderSummary>> liveOrderBoard = LiveOrderBoard
            .createWithOrderSummaryRenderer(mockOrderSummaryRenderer)
            .withOrderRepository(mockOrderRepository);

    @Test
    public void shouldInsertOrderWhenRegisterCalled() {
        when(mockOrderRepository.insert(sellOrder)).thenReturn(sellOrder);

        Order registeredOrder = liveOrderBoard.register(sellOrder);

        assertEquals(sellOrder, registeredOrder);
        verify(mockOrderRepository).insert(sellOrder);
    }

    @Test
    public void shouldDeleteOrderWhenCancelCalled() {
        when(mockOrderRepository.delete(sellOrder.getId())).thenReturn(of(sellOrder));

        Order cancelledOrder = liveOrderBoard.cancel(sellOrder.getId());

        assertEquals(sellOrder, cancelledOrder);
        verify(mockOrderRepository).delete(sellOrder.getId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void shouldThrowOrderNotFoundExceptionWhenOrderRepositoryReturnsEmptyOptional() {
        when(mockOrderRepository.delete(sellOrder.getId())).thenReturn(empty());

        liveOrderBoard.cancel(sellOrder.getId());
    }

    @Test
    public void shouldReturnEmptySummaryWhenOrderHasNotBeenRegisteredYet() {
        when(mockOrderRepository.getOrdersByType(SELL)).thenReturn(EMPTY_LIST);
        when(mockOrderSummaryRenderer.render(EMPTY_LIST)).thenReturn(EMPTY_LIST);

        List<OrderSummary> orderSummaries = liveOrderBoard.summary(SELL);

        assertEquals(0, orderSummaries.size());
        verify(mockOrderRepository).getOrdersByType(SELL);
        verify(mockOrderSummaryRenderer).render(EMPTY_LIST);
    }

    @Test
    public void shouldReturnSellOrderSummarySortedByPrice() {
        List<Order> orders = asList(sellOrder, sellOrderWithGreaterPrice);
        when(mockOrderRepository.getOrdersByType(SELL)).thenReturn(orders);

        List<OrderSummary> orderSummaries = asList(
                createOrderSummary(3.5, 306),
                createOrderSummary(1.2, 310)
        );
        when(mockOrderSummaryRenderer.render(orderSummaries)).thenReturn(orderSummaries);

        List<OrderSummary> result = liveOrderBoard.summary(SELL);

        assertEquals(2, orderSummaries.size());
        assertEquals(orderSummaries, result);
        verify(mockOrderRepository).getOrdersByType(SELL);
        verify(mockOrderSummaryRenderer).render(orderSummaries);
    }

    @Test
    public void shouldReturnBuyOrderSummaryReverseSortedByPrice() {
        List<Order> orders = asList(buyOrder, buyOrderWithGreaterPrice);
        when(mockOrderRepository.getOrdersByType(BUY)).thenReturn(orders);

        List<OrderSummary> orderSummaries = asList(
                createOrderSummary(1.2, 310),
                createOrderSummary(3.5, 306)
        );
        when(mockOrderSummaryRenderer.render(orderSummaries)).thenReturn(orderSummaries);

        List<OrderSummary> result = liveOrderBoard.summary(BUY);

        assertEquals(2, orderSummaries.size());
        assertEquals(orderSummaries, result);
        verify(mockOrderRepository).getOrdersByType(BUY);
        verify(mockOrderSummaryRenderer).render(orderSummaries);
    }

    @Test
    public void shouldReturnOrderSummaryWithTotalWhenPriceIsTheSame() {
        List<Order> orders = asList(sellOrder, anotherSellOrderWithSamePrice);
        when(mockOrderRepository.getOrdersByType(SELL)).thenReturn(orders);

        List<OrderSummary> orderSummaries = singletonList(createOrderSummary(5, 306));
        when(mockOrderSummaryRenderer.render(orderSummaries)).thenReturn(orderSummaries);

        List<OrderSummary> result = liveOrderBoard.summary(SELL);

        assertEquals(1, orderSummaries.size());
        assertEquals(orderSummaries, result);
        verify(mockOrderRepository).getOrdersByType(SELL);
        verify(mockOrderSummaryRenderer).render(orderSummaries);
    }

    @Test(expected = InvalidParameterException.class)
    public void shouldThrowInvalidParameterExceptionWhenGivenOrderSummaryRenderIsNull() {
        LiveOrderBoard.createWithOrderSummaryRenderer(null);
    }

    @Test(expected = InvalidParameterException.class)
    public void shouldThrowInvalidParameterExceptionWhenGivenOrderRepositoryIsNull() {
        LiveOrderBoard.createWithLineListOrderSummaryRenderer().withOrderRepository(null);
    }

    private static OrderSummary createOrderSummary(double quantity, double price) {
        return OrderSummary.createOrderSummary(valueOf(quantity), valueOf(price));
    }

}