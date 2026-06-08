package com.vaishnav.airlinq.payment.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.vaishnav.airlinq.payment.client.BookingServiceClient;
import com.vaishnav.airlinq.payment.config.RazorpayConfig;
import com.vaishnav.airlinq.payment.mapper.PaymentMapper;
import com.vaishnav.airlinq.payment.model.Payment;
import com.vaishnav.airlinq.payment.repository.PaymentRepository;
import com.vaishnav.airlinq.payment.service.PaymentService;
import com.vaishnav.enums.BookingStatus;
import com.vaishnav.enums.PaymentGateway;
import com.vaishnav.enums.PaymentStatus;
import com.vaishnav.payload.request.BookingConfirmRequest;
import com.vaishnav.payload.request.PaymentCreateRequest;
import com.vaishnav.payload.request.RazorpayVerifyRequest;
import com.vaishnav.payload.response.BookingResponse;
import com.vaishnav.payload.response.PaymentResponse;
import com.vaishnav.payload.response.RazorpayOrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final RazorpayConfig razorpayConfig;
    private final BookingServiceClient bookingServiceClient;

    @Override
    @Transactional
    public RazorpayOrderResponse createRazorpayOrder(
            Long userId,
            PaymentCreateRequest paymentCreateRequest
    ) throws Exception {

        if(paymentRepository.findByBookingId(paymentCreateRequest.getBookingId()).isPresent()) {
            throw new Exception("Payment already exists with booking id");
        }

        String currency = paymentCreateRequest.getCurrency() != null ?
                paymentCreateRequest.getCurrency() : razorpayConfig.getCurrency();

        Integer amountInPaise = toPaise(paymentCreateRequest.getAmount());
        String receipt = generateReceipt();

        JSONObject objectResponse = new JSONObject();
        objectResponse.put("amount", amountInPaise);
        objectResponse.put("currency", currency);
        objectResponse.put("receipt", receipt);
        objectResponse.put("payment_capture", 1);

        Order razorpayOrder = razorpayClient.orders.create(objectResponse);

        Payment payment = Payment.builder()
                .bookingId(paymentCreateRequest.getBookingId())
                .userId(userId)
                .receipt(receipt)
                .amount(paymentCreateRequest.getAmount())
                .currency(currency)
                .gateway(PaymentGateway.RAZORPAY)
                .status(PaymentStatus.CREATED)
                .razorpayOrderId(razorpayOrder.get("id"))
                .amountInPaise(amountInPaise)
                .isActive(true)
                .build();

        payment = paymentRepository.save(payment);

        return RazorpayOrderResponse.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBookingId())
                .keyId(razorpayConfig.getKeyId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .receipt(payment.getReceipt())
                .amount(payment.getAmount())
                .amountInPaise(amountInPaise)
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .build();
    }

    @Override
    @Transactional
    public PaymentResponse verifyRazorpayPayment(RazorpayVerifyRequest request) throws Exception {
        Payment payment  = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new Exception("Payment not found with id: " + request.getPaymentId()));

        if(payment.getStatus() == PaymentStatus.PAID) {
            return PaymentMapper.toResponse(payment);
        }

        if(!payment.getRazorpayOrderId().equals(request.getRazorpayOrderId())) {
            throw new Exception("Razorpay Order Id doesn't match");
        }

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());

        Utils.verifyPaymentSignature(
                options,
                razorpayConfig.getKeySecret()
        );

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(Instant.now());
        payment.setFailureReason("NA");

        payment = paymentRepository.save(payment);

        BookingResponse booking = bookingServiceClient.getBookingById(payment.getBookingId());
        if(booking == null || booking.getId()==null) {
            throw new Exception("Booking not found with id: " + payment.getBookingId());
        }
        if(!booking.getUserId().equals(payment.getUserId())) {
            throw new Exception("Booking does not belongs to payment user");
        }
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new Exception("Only Pending Bookings can be confirmed");
        }
        if(!booking.getTotalAmount().equals(payment.getAmount())) {
            throw new Exception("Payment amount does not match with Booking Amounts");
        }

        BookingConfirmRequest confirmRequest = BookingConfirmRequest.builder()
                .paymentId(payment.getId())
                .build();
        bookingServiceClient.confirmBooking(booking.getId(), confirmRequest);

        return PaymentMapper.toResponse(payment);

    }

    @Override
    public PaymentResponse getByBookingId(Long bookingId) throws Exception {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new Exception("Payment not found with booking id " + bookingId));
        return PaymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getByPaymentId(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with payment id " + paymentId));
        return PaymentMapper.toResponse(payment);
    }

    @Override
    public Page<PaymentResponse> getByUserId(Long userId, Pageable pageable) throws Exception {
        return paymentRepository.findByUserId(userId, pageable)
                .map(PaymentMapper::toResponse);
    }

    @Override
    public PaymentResponse markFailedPayment(Long paymentId, String reason) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with payment id " + paymentId));

        if(payment.getStatus() == PaymentStatus.PAID) {
            throw new Exception("Payment is already paid cannot be marked as failed " );
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(reason);

        payment = paymentRepository.save(payment);
        return PaymentMapper.toResponse(payment);
    }

    private Integer toPaise(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValueExact();
    }

    private String generateReceipt() {
        return "ALQ-PAY-" + UUID.randomUUID()
                .toString()
                .substring(0, 10)
                .toUpperCase();
    }

}
