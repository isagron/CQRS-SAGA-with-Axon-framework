package com.isagron.estore.ProductService.services.commands;

import com.isagron.estore.ProductService.api.dto.CreateProductReq;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Builder
@Data
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private final String productId;

    private final String title;

    private final Integer price;

    private final Integer quantity;

    public static CreateProductCommand createFromReq(CreateProductReq req){
        return CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(req.getTitle())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .build();
    }
}
