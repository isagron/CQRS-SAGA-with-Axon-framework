package com.isagron.estore.OrderService.service.query;

import com.isagron.estore.OrderService.api.dto.OrderSummary;
import com.isagron.estore.OrderService.dao.OrderRepository;
import com.isagron.estore.OrderService.domain.OrderEntity;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryHandler {

    @Autowired
    private OrderRepository orderRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery){
        OrderEntity orderEntity = orderRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), null);
    }
}
