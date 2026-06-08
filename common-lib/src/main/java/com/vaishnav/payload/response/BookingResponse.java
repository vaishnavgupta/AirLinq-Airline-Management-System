package com.vaishnav.payload.response;

import com.vaishnav.enums.BookingStatus;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {

    private Long id;

    private String bookingReference;

    private Long userId;

    private FlightInstanceResponse flightInstance;

    private FareResponse fare;

    private CabinClass cabinClass;

    private Integer passengerCount;

    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal serviceFee;

    private BigDecimal totalAmount;

    private String currency;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private Instant expiresAt;

    private Boolean isActive;

    private List<BookingPassengerResponse> passengers;

    private Instant createdAt;

    private Instant updatedAt;
}
