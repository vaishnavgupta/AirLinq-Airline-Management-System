package com.vaishnav.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlineDropDownItem {
    private Long id;
    private String name;
    private String iataCode;
    private String icaoCode;
    private String logoUrl;
    private String country = "India";
}
