package com.vaishnav.payload.response;

import com.vaishnav.embeddable.Support;
import com.vaishnav.enums.AirlineStatus;
import com.vaishnav.payload.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResponse {
    private Long id;

    private String iataCode;

    private String icaoCode;

    private String name;

    private String alias;

    private String logoUrl;

    private String website;

    private AirlineStatus status;

    private String alliance;

    private LocalDateTime  createdAt;

    private LocalDateTime updatedAt;

    private Long ownerId;

    private UserDto owner;

    private CityResponse headquarterCity;

    private Support support;
}
