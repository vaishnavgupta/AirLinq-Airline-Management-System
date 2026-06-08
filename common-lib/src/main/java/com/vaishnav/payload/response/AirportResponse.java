package com.vaishnav.payload.response;

import com.vaishnav.embeddable.Address;
import com.vaishnav.embeddable.GeoCode;import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportResponse {

    private Long id;
    private String iataCode;
    private String name;
    private String detailedName;
    private ZoneId timeZone;
    private Address address;
    private CityResponse cityResponse;
    private GeoCode  geoCode;

}
