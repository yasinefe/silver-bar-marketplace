package com.yefe.silverbar.marketplace.repository;

import com.yefe.silverbar.marketplace.domain.Order;
import org.junit.Test;

import java.util.Optional;

import static com.yefe.silverbar.marketplace.domain.OrderType.BUY;
import static com.yefe.silverbar.marketplace.domain.OrderType.SELL;
import static org.junit.Assert.*;

public class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository inMemoryOrderRepository = new InMemoryOrderRepository();

    private Order buyOrder = Order.createOrder("user-id", 3.5, 306, BUY);
    private Order sellOrder = Order.createOrder("user-id", 3.5, 306, SELL);

    @Test
    public void shouldInsertAnOrder() {
        Order insertedOrder = inMemoryOrderRepository.insert(buyOrder);

        assertEquals(buyOrder, insertedOrder);
        assertEquals(buyOrder, inMemoryOrderRepository.getOrdersByType(BUY).get(0));
    }

    @Test
    public void shouldDeleteOrder() {
        inMemoryOrderRepository.insert(buyOrder);
        Optional<Order> deletedOrder = inMemoryOrderRepository.delete(buyOrder.getId());

        assertTrue(deletedOrder.isPresent());
        assertEquals(buyOrder, deletedOrder.get());
        assertEquals(0, inMemoryOrderRepository.getOrdersByType(BUY).size());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenDeleteOrderIsCalledWithNonExistingOrderId() {
        Optional<Order> deletedOrder = inMemoryOrderRepository.delete("non existing order id");

        assertFalse(deletedOrder.isPresent());
    }

    @Test
    public void shouldReturnAllOrdersWithType() {
        inMemoryOrderRepository.insert(buyOrder);
        inMemoryOrderRepository.insert(sellOrder);

        assertEquals(1, inMemoryOrderRepository.getOrdersByType(BUY).size());
        assertEquals(buyOrder, inMemoryOrderRepository.getOrdersByType(BUY).get(0));
        assertEquals(1, inMemoryOrderRepository.getOrdersByType(SELL).size());
        assertEquals(sellOrder, inMemoryOrderRepository.getOrdersByType(SELL).get(0));
    }
}