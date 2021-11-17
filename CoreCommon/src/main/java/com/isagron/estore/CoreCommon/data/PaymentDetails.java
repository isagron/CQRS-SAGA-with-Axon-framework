package com.isagron.estore.CoreCommon.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetails {

    private final String name;
    private final String cardNumber;
    private final int validUntilMonth;
    private final int validUntilYear;
    private final String cvv;


    public static PaymentDetails mock() {
        return PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();
    }
}
