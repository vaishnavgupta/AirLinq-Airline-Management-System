package com.vaishnav.payload.request;

import com.vaishnav.enums.CabinClass;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull(message = "Fare ID is required")
    private Long fareId;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<BookingPassengerRequest> passengers;

}
