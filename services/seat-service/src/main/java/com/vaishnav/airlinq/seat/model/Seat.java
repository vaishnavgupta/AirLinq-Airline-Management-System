package com.vaishnav.airlinq.seat.model;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SeatMap seatMap;

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

    @Builder.Default
    private Boolean isExitRow = false;

    @Builder.Default
    private Boolean hasExtraLegroom = false;

    @Builder.Default
    private Boolean isActive = true;
}
