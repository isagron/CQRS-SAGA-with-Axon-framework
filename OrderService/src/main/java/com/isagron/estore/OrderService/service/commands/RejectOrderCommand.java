package com.isagron.estore.OrderService.service.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@AllArgsConstructor
public class RejectOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;

    private  final String reason;
}
