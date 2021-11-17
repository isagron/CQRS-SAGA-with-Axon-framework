package com.isagron.estore.OrderService.service.saga;

import com.isagron.estore.CoreCommon.commands.CancelProductReservationCommand;
import com.isagron.estore.CoreCommon.commands.ProcessPaymentCommand;
import com.isagron.estore.CoreCommon.commands.ReserveProductCommand;
import com.isagron.estore.CoreCommon.data.User;
import com.isagron.estore.CoreCommon.evnets.ProductReservationCancelledEvent;
import com.isagron.estore.CoreCommon.evnets.ProductReservedEvent;
import com.isagron.estore.OrderService.api.dto.OrderSummary;
import com.isagron.estore.OrderService.service.commands.ApproveOrderCommand;
import com.isagron.estore.OrderService.service.commands.RejectOrderCommand;
import com.isagron.estore.OrderService.service.core.events.OrderApproveEvent;
import com.isagron.estore.OrderService.service.core.events.OrderCreatedEvent;
import com.isagron.estore.OrderService.service.core.events.OrderRejectedEvent;
import com.isagron.estore.OrderService.service.core.events.PaymentProcessedEvent;
import com.isagron.estore.OrderService.service.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class OrderSaga {

    public static final String PAYMENT_DEADLINE ="payment-processing-deadline";

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        log.info("OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId());

        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

            @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                 CommandResultMessage<? extends Object> commandResultMessage) {
                if(commandResultMessage.isExceptional()) {
                    // Start a compensating transaction
                    RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(),
                            commandResultMessage.exceptionResult().getMessage());

                    commandGateway.send(rejectOrderCommand);
                }

            }

        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("ProductReservedEvent handled for orderId: " + productReservedEvent.getOrderId());

        User user = User.mock();

        log.info("User details: " + user.getFirstName());

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(user.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand);

            deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS),
                    PAYMENT_DEADLINE, productReservedEvent);

        } catch (Exception ex){
            //start compensation transaction
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }

        if (result == null){
            log.error("The process payment command result with a null");
            //start compensation transaction
            cancelProductReservation(productReservedEvent, "Failed to process payment");
            return;
        }

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("Order saga - process event PaymentProcessedEvent order id: " + paymentProcessedEvent.getOrderId());

        deadlineManager.cancelAll(PAYMENT_DEADLINE);
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());

        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApproveEvent orderApproveEvent) {
        log.info("Order saga is complete for order id: " + orderApproveEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class,
                query -> true,
                new OrderSummary(orderApproveEvent.getOrderId(),
                        orderApproveEvent.getOrderStatus(),
                        null));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent event) {
        log.info("Order saga handle ProductReservationCancelledEvent: " + event.getOrderId());
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId(),
                event.getReason());

        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent){
        log.info("Order saga - order was rejected: " + orderRejectedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class,
                query -> true,
                new OrderSummary(orderRejectedEvent.getOrderId(),
                        orderRejectedEvent.getOrderStatus(),
                        orderRejectedEvent.getReason()));
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason){

        log.info("In saga class, cancel product reservation: " + event.getProductId());
        CancelProductReservationCommand cancelProductReservation = CancelProductReservationCommand.builder()
                .productId(event.getProductId())
                .orderId(event.getOrderId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservation);
    }

    @DeadlineHandler(deadlineName = PAYMENT_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent event){
        log.info("Payment deadline took place, sending compensation");
        cancelProductReservation(event, "Timeout");
    }
}
