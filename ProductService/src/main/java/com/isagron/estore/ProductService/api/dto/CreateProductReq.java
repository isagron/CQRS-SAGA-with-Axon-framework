package com.isagron.estore.ProductService.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductReq {

    @NotBlank(message = "Product title is a required field")
    private String title;

    @Min(1)
    private Integer price;

    private Integer quantity;
}
