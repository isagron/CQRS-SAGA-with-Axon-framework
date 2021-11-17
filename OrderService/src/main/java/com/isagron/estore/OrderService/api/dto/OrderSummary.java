package com.isagron.estore.OrderService.api.dto;

import lombok.Value;

@Value
public class OrderSummary {

    private final String orderId;

    private final OrderStatus orderStatus;

    private final String reason;
}
