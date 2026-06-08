package com.vaishnav.airlinq.booking.model;

import com.vaishnav.enums.PassengerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Booking booking;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassengerType passengerType;

    private String gender;

    private String email;

    private String phone;

    private String passportNumber;

    private Long seatInstanceId;

    private String seatNumber;
}
