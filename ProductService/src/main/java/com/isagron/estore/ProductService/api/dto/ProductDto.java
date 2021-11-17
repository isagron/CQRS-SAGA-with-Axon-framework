package com.isagron.estore.ProductService.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productId;

    private String title;

    private Integer price;

    private Integer quantity;
}
