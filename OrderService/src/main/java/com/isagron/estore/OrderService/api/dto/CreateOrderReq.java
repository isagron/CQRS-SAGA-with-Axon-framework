package com.isagron.estore.OrderService.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateOrderReq {
    @NotBlank(message = "Order productId is a required field")
    private final String productId;
    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 5, message = "Quantity cannot be larger than 5")
    private final int quantity;
    @NotBlank(message = "Order addressId is a required field")
    private final String addressId;
}
