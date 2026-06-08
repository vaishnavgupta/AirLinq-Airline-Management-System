package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatMapRequest {

    private Long airlineId;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotBlank(message = "Seat map name is required")
    private String name;

    @Positive
    private Integer totalRows;

    @Positive
    private Integer totalSeats;

    private Boolean isActive;
}
