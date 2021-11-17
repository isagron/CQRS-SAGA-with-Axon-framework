package com.isagron.estore.OrderService.service.core.events;

import com.isagron.estore.OrderService.api.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRejectedEvent {

    private String orderId;

    private  String reason;
    private OrderStatus orderStatus = OrderStatus.REJECTED;
}
