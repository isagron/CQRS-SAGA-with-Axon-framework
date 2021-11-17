package com.isagron.estore.OrderService.service.core.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentProcessedEvent {

    private String orderId;
    private String paymentId;
}
