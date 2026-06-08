package com.vaishnav.airlinq.seat.model;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatInstanceStatus;
import com.vaishnav.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SeatInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long airlineId;

    @Column(nullable = false)
    private Long flightInstanceId;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private Long flightInstanceCabinId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(name = "seat_row_number", nullable = false)
    private Integer rowNumber;

    @Column(nullable = false)
    private String seatColumn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabinClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatInstanceStatus status;

    private Long bookingId;

    private Long passengerId;

    private BigDecimal seatFee;

    private Boolean isExitRow;

    private Boolean hasExtraLegroom;

    private Instant lockedUntil;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
