package com.isagron.estore.OrderService.service.commands;

import com.isagron.estore.OrderService.api.dto.CreateOrderReq;
import com.isagron.estore.OrderService.api.dto.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    public final String orderId;
    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId;
    private final OrderStatus orderStatus;


    public static CreateOrderCommand createFromReq(CreateOrderReq createOrderReq) {
        return new CreateOrderCommand(UUID.randomUUID().toString(),
                "27b95829-4f3f-4ddf-8983-151ba010e35b",
                createOrderReq.getProductId(),
                createOrderReq.getQuantity(),
                createOrderReq.getAddressId(),
                OrderStatus.CREATED);
    }
}
