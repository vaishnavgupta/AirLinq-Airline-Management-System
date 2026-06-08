package com.vaishnav.payload.request;

import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatRequest {

    @NotNull(message = "Seat Map Id is required")
    private Long seatMapId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Row number is required")
    @Positive
    private Integer rowNumber;

    @NotBlank(message = "Seat column is required")
    private String seatColumn;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;

    private Boolean isExitRow;

    private Boolean hasExtraLegroom;

    private Boolean isActive;

}
