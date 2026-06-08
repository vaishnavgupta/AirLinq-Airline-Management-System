package com.vaishnav.airlinq.payment.repository;

import com.vaishnav.airlinq.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Page<Payment> findByUserId(Long userId, Pageable pageable);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
}
