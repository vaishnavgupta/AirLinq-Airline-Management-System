package com.vaishnav.payload.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatMapResponse {

    private Long id;

    private AirlineResponse airline;

    private AircraftResponse aircraft;

    private String name;

    private Integer totalRows;

    private Integer totalSeats;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
