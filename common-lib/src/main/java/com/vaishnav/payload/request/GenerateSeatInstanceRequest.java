package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateSeatInstanceRequest {
    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    private Long airlineId;
}
