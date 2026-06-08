package com.vaishnav.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingConfirmRequest {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;
}
