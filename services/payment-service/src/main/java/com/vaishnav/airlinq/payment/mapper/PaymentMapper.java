package com.vaishnav.airlinq.payment.mapper;

import com.vaishnav.airlinq.payment.model.Payment;
import com.vaishnav.payload.response.PaymentResponse;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment payment) {
        if(payment == null) return null;

        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .receipt(payment.getReceipt())
                .amount(payment.getAmount())
                .amountInPaise(payment.getAmountInPaise())
                .currency(payment.getCurrency())
                .gateway(payment.getGateway())
                .status(payment.getStatus())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .razorpayPaymentId(payment.getRazorpayPaymentId())
                .failureReason(payment.getFailureReason())
                .paidAt(payment.getPaidAt())
                .isActive(payment.getIsActive())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

}
