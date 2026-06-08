package com.vaishnav.airlinq.booking.model;

import com.vaishnav.enums.BookingStatus;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingReference;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long flightInstanceId;

    @Column(nullable = false)
    private Long fareId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabinClass;

    @Column(nullable = false)
    private Integer passengerCount;

    @Column(nullable = false)
    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal serviceFee;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private Instant expiresAt;

    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookingPassenger> passengers = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
