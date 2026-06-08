package com.vaishnav.payload.request;

import com.vaishnav.embeddable.Address;
import com.vaishnav.embeddable.GeoCode;import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportRequest {

    @NotBlank(message = "IATA Code is mandatory")
    @Size(min = 3, max = 3, message = "Must have three characters")
    private String iataCode;

    @NotBlank(message = "Airport Name is mandatory")
    private String name;

    private ZoneId timeZone;

    @Valid
    private Address address;

    @NotNull(message = "City ID is mandatory")
    private Long cityId;

    @Valid
    private GeoCode geoCode;
}
