package com.isagron.estore.OrderService.service.core.events;

import com.isagron.estore.OrderService.api.dto.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderCreatedEvent {

    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
