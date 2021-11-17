package com.isagron.estore.CoreCommon.evnets;

import lombok.Data;

@Data
public class ProductReservationCancelledEvent {

    private String productId;
    private Integer quantity;
    private String orderId;
    private String userId;
    private String reason;


}
