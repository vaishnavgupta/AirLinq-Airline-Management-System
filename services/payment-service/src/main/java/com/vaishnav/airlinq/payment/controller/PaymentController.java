package com.vaishnav.airlinq.payment.controller;

import com.vaishnav.airlinq.payment.service.PaymentService;
import com.vaishnav.payload.request.PaymentCreateRequest;
import com.vaishnav.payload.request.RazorpayVerifyRequest;
import com.vaishnav.payload.response.PaymentResponse;
import com.vaishnav.payload.response.RazorpayOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/razorpay/order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody PaymentCreateRequest request
    ) throws Exception {
        return paymentService.createRazorpayOrder(userId, request);
    }

    @PostMapping("/razorpay/verify")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse verifyRazorpayPayment(
            @Valid @RequestBody RazorpayVerifyRequest request
    ) throws Exception {
        return paymentService.verifyRazorpayPayment(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse getPaymentById(
            @PathVariable Long id
    ) throws Exception {
        return paymentService.getByPaymentId(id);
    }

    @GetMapping("/booking/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse getPaymentByBookingId(
            @PathVariable Long bookingId
    ) throws Exception {
        return paymentService.getByBookingId(bookingId);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponse> getPaymentsByUserId(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable
    ) throws Exception {
        return paymentService.getByUserId(userId, pageable);
    }

    @PostMapping("/{id}/failed")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse markPaymentFailed(
            @PathVariable Long id,
            @RequestParam(required = false) String reason
    ) throws Exception {
        return paymentService.markFailedPayment(id, reason);
    }

}
