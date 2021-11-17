package com.isagron.estore.CoreCommon.evnets;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvent {

    private String productId;
    private Integer quantity;
    private String orderId;
    private String userId;
}
