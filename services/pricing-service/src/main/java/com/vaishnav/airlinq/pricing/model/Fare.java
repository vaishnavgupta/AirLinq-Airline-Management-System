package com.vaishnav.airlinq.pricing.model;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Fare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long airlineId;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private Long flightInstanceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabinClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FareType fareType;

    @Column(nullable = false)
    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal serviceFee;

    private BigDecimal totalFare;

    @Column(nullable = false)
    private String currency;

    private Integer availableSeats;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FareStatus status;

    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Transient
    public BigDecimal getAddedTotalFare() {
        return baseFare.add(taxAmount).add(serviceFee) ;
    }

}
