package com.vaishnav.airlinq.booking.repository;

import com.vaishnav.airlinq.booking.model.Booking;
import com.vaishnav.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    List<Booking> findByBookingStatusAndExpiresAtBefore(
            BookingStatus bookingStatus,
            Instant expiresAt
    );

}
