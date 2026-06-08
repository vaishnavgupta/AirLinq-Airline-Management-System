package com.vaishnav.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityResponse {
    private Long id;

    private String name;

    private String cityCode;

    private String countryCode;

    private String countryName;

    private String regionCode;

    private String timeZoneId;
}
