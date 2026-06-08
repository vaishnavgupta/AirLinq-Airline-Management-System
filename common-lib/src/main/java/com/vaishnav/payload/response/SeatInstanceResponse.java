package com.vaishnav.payload.response;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatInstanceStatus;
import com.vaishnav.enums.SeatType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatInstanceResponse {
    private Long id;

    private Long airlineId;

    private FlightInstanceResponse flightInstance;

    private Long seatId;

    private Long flightInstanceCabinId;

    private String seatNumber;

    private Integer rowNumber;

    private String seatColumn;

    private CabinClass cabinClass;

    private SeatType seatType;

    private SeatInstanceStatus status;

    private Long bookingId;

    private Long passengerId;

    private BigDecimal seatFee;

    private Boolean isExitRow;

    private Boolean hasExtraLegroom;

    private Instant lockedUntil;

    private Instant createdAt;

    private Instant updatedAt;
}
