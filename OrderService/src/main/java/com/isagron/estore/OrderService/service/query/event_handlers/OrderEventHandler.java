package com.isagron.estore.OrderService.service.query.event_handlers;

import com.isagron.estore.OrderService.dao.OrderRepository;
import com.isagron.estore.OrderService.domain.OrderEntity;
import com.isagron.estore.OrderService.service.core.events.OrderApproveEvent;
import com.isagron.estore.OrderService.service.core.events.OrderCreatedEvent;
import com.isagron.estore.OrderService.service.core.events.OrderRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("order-group")
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent event){
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApproveEvent event){
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event){
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }
}
