package com.isagron.estore.OrderService.service.core.events;

import com.isagron.estore.OrderService.api.dto.OrderStatus;
import lombok.Value;

@Value
public class OrderApproveEvent {

    private String orderId;

    private OrderStatus orderStatus;
}
