package com.vaishnav.payload.response;

import com.vaishnav.enums.PaymentGateway;
import com.vaishnav.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Long userId;
    private String receipt;
    private BigDecimal amount;
    private Integer amountInPaise;
    private String currency;
    private PaymentGateway gateway;
    private PaymentStatus status;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String failureReason;
    private String refundId;
    private Instant paidAt;
    private Instant refundedAt;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

}
