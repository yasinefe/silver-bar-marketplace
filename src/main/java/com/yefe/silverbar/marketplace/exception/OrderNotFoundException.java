package com.yefe.silverbar.marketplace.exception;

public class OrderNotFoundException extends ServiceException {

    public OrderNotFoundException(String orderId) {
        super("Order not found with id: " + orderId);
    }

}
