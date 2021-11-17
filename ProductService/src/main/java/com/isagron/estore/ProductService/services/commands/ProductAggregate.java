package com.isagron.estore.ProductService.services.commands;

import com.isagron.estore.CoreCommon.commands.CancelProductReservationCommand;
import com.isagron.estore.CoreCommon.commands.ReserveProductCommand;
import com.isagron.estore.CoreCommon.evnets.ProductReservationCancelledEvent;
import com.isagron.estore.CoreCommon.evnets.ProductReservedEvent;
import com.isagron.estore.ProductService.services.events.ProductCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@NoArgsConstructor
@Slf4j
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private Integer price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        log.info("Handle create product command, for product: " + command.getProductId());

        //validate create product command
        ProductCreatedEvent event = new ProductCreatedEvent();
        //copy the play load of the event
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);

    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        if (quantity < reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservation){
        log.info("Handle cancel product command, for product: " + cancelProductReservation.getProductId());

        ProductReservationCancelledEvent event = new ProductReservationCancelledEvent();
        BeanUtils.copyProperties(cancelProductReservation, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event){
        this.productId = event.getProductId();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
        this.title = event.getTitle();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){
        this.quantity -= productReservedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent event){
        this.quantity += event.getQuantity();
    }
}
