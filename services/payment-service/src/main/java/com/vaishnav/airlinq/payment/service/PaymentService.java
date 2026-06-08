package com.vaishnav.airlinq.payment.service;

import com.vaishnav.payload.request.PaymentCreateRequest;
import com.vaishnav.payload.request.RazorpayVerifyRequest;
import com.vaishnav.payload.response.PaymentResponse;
import com.vaishnav.payload.response.RazorpayOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    RazorpayOrderResponse createRazorpayOrder(
            Long userId,
            PaymentCreateRequest  paymentCreateRequest
    ) throws Exception;

    PaymentResponse verifyRazorpayPayment(RazorpayVerifyRequest request) throws Exception;

    PaymentResponse getByBookingId(Long bookingId) throws Exception;

    PaymentResponse getByPaymentId(Long paymentId) throws Exception;

    Page<PaymentResponse> getByUserId(Long userId, Pageable pageable) throws Exception;

    PaymentResponse markFailedPayment(Long paymentId, String reason) throws Exception;
}
