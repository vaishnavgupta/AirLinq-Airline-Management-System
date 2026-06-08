package com.vaishnav.payload.request;

import com.vaishnav.enums.PassengerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassengerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Passenger type is required")
    private PassengerType passengerType;

    private String gender;

    private String email;

    private String phone;

    private String passportNumber;

    @NotNull(message = "Seat Instance Id is required")
    private Long seatInstanceId;

    @NotNull(message = "Seat Number is required")
    private String seatNumber;

}
