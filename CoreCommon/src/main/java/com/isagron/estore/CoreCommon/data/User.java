package com.isagron.estore.CoreCommon.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;

    public static User mock(){
        return User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .paymentDetails(PaymentDetails.mock())
                .build();
    }
}
