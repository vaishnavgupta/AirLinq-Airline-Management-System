package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityRequest {
    @NotBlank(message = "City Name is required")
    @Size(min = 3, max = 30, message = "Must have between 3 to 30 characters")
    private String name;

    @NotBlank(message = "City Code is required")
    @Size(max = 5, message = "Must have 5 characters")
    private String cityCode;

    @NotBlank(message = "Country Code is required")
    @Size(max = 5, message = "Must have 5")
    private String countryCode;

    @NotBlank(message = "Country Name is required")
    @Size(min = 3, max = 30, message = "Must have between 3 to 30 characters")
    private String countryName;

    @NotBlank(message = "Region Code is required")
    @Size(max = 10)
    private String regionCode;

    @NotBlank(message = "Time Zone Id is required")
    @Size(max = 50)
    private String timeZoneId;
}
