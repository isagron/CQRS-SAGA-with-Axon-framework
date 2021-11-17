package com.isagron.estore.ProductService.services.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {

    private String productId;

    private String title;

    private Integer price;

    private Integer quantity;
}
