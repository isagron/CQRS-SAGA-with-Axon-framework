package com.isagron.estore.OrderService.service.query.event_handlers;

import com.isagron.estore.OrderService.dao.OrderRepository;
import com.isagron.estore.OrderService.dao.PaymentRepository;
import com.isagron.estore.OrderService.domain.OrderEntity;
import com.isagron.estore.OrderService.domain.PaymentEntity;
import com.isagron.estore.OrderService.service.core.events.OrderCreatedEvent;
import com.isagron.estore.OrderService.service.core.events.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("payment-group")
public class PaymentEventHandler {

    private final PaymentRepository orderRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event){
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(event, paymentEntity);
        orderRepository.save(paymentEntity);
    }
}
