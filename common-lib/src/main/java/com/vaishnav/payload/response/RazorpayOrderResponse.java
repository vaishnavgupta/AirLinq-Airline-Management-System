package com.vaishnav.payload.response;

import com.vaishnav.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RazorpayOrderResponse {

    private Long paymentId;
    private Long bookingId;
    private String keyId;
    private String razorpayOrderId;
    private String receipt;
    private BigDecimal amount;
    private Integer amountInPaise;
    private String currency;
    private PaymentStatus status;

}
