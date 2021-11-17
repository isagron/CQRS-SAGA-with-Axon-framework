package com.isagron.estore.OrderService.api.controllers;

import com.isagron.estore.OrderService.api.dto.CreateOrderReq;
import com.isagron.estore.OrderService.api.dto.OrderSummary;
import com.isagron.estore.OrderService.service.commands.CreateOrderCommand;
import com.isagron.estore.OrderService.service.query.FindOrderQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands/orders")
@RequiredArgsConstructor
public class OrderCommandsController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@RequestBody CreateOrderReq createOrderReq){

        CreateOrderCommand command = CreateOrderCommand.createFromReq(createOrderReq);

        SubscriptionQueryResult<OrderSummary, OrderSummary> result = queryGateway.subscriptionQuery(
                new FindOrderQuery(command.getOrderId()),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class)
                );
        try {
            commandGateway.sendAndWait(command);
            return result.updates().blockFirst();
        } finally {
            result.close();
        }
    }
}
