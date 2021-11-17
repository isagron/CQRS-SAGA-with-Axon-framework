package com.isagron.estore.OrderService.service.commands;

import com.isagron.estore.CoreCommon.commands.ProcessPaymentCommand;
import com.isagron.estore.OrderService.service.core.events.OrderCreatedEvent;
import com.isagron.estore.OrderService.service.core.events.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class PaymentAggregate {

    private String orderId;
    @AggregateIdentifier
    private String paymentId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command){
        log.info("PaymentAggregate - handle ProcessPaymentCommand, for order: " + command.getOrderId());

        //validate create product command
        PaymentProcessedEvent event = new PaymentProcessedEvent();
        //copy the play load of the event
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);

    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event){
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
    }
}
