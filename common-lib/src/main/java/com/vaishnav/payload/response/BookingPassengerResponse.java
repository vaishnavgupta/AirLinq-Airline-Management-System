package com.vaishnav.payload.response;

import com.vaishnav.enums.PassengerType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassengerResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private PassengerType passengerType;

    private String gender;

    private String email;

    private String phone;

    private String passportNumber;

    private Long seatInstanceId;

    private String seatNumber;
}
