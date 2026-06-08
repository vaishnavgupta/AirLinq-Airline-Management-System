package com.vaishnav.payload.response;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatResponse {

    private Long id;

    private Long seatMapId;

    private String seatNumber;

    private Integer rowNumber;

    private String seatColumn;

    private CabinClass cabinClass;

    private SeatType seatType;

    private Boolean isExitRow;

    private Boolean hasExtraLegroom;

    private Boolean isActive;

}
