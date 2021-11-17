package com.isagron.estore.OrderService.service.commands;

import com.isagron.estore.OrderService.api.dto.OrderStatus;
import com.isagron.estore.OrderService.service.core.events.OrderApproveEvent;
import com.isagron.estore.OrderService.service.core.events.OrderCreatedEvent;
import com.isagron.estore.OrderService.service.core.events.OrderRejectedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Slf4j
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command){
        log.info("OrderAggregate - handle CreateOrderCommand, for order: " + command.getOrderId());
        //validate create product command
        OrderCreatedEvent event = new OrderCreatedEvent();
        //copy the play load of the event
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);

    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand){
        log.info("OrderAggregate - handle ApproveOrderCommand, for order: " + approveOrderCommand.getOrderId());

        OrderApproveEvent orderApproveEvent = new OrderApproveEvent(approveOrderCommand.getOrderId(), OrderStatus.APPROVED);

        AggregateLifecycle.apply(orderApproveEvent);
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand){
        log.info("OrderAggregate - handle RejectOrderCommand, for order: " + rejectOrderCommand.getOrderId());

        OrderRejectedEvent event = new OrderRejectedEvent(rejectOrderCommand.getOrderId(),
                rejectOrderCommand.getReason(), OrderStatus.REJECTED);
        AggregateLifecycle.apply(event);

    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderApproveEvent){
        this.orderStatus = orderApproveEvent.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderApproveEvent orderApproveEvent){
        this.orderStatus = orderApproveEvent.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event){
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();

    }


}
