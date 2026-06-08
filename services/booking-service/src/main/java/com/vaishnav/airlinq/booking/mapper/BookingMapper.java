package com.vaishnav.airlinq.booking.mapper;

import com.vaishnav.airlinq.booking.model.Booking;
import com.vaishnav.airlinq.booking.model.BookingPassenger;
import com.vaishnav.payload.request.BookingPassengerRequest;
import com.vaishnav.payload.response.BookingPassengerResponse;
import com.vaishnav.payload.response.BookingResponse;
import com.vaishnav.payload.response.FareResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;

import java.util.List;

public class BookingMapper {

    public static BookingPassenger toBookingPassenger(
            BookingPassengerRequest request
    ) {
        if(request == null) return null;

        return BookingPassenger.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .passengerType(request.getPassengerType())
                .gender(request.getGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passportNumber(request.getPassportNumber())
                .seatInstanceId(request.getSeatInstanceId())
                .seatNumber(request.getSeatNumber())
                .build();
    }

    public static BookingPassengerResponse toBookingPassengerResponse(
        BookingPassenger passenger
    ) {
        if(passenger == null) return null;

        return BookingPassengerResponse.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .dateOfBirth(passenger.getDateOfBirth())
                .passengerType(passenger.getPassengerType())
                .gender(passenger.getGender())
                .email(passenger.getEmail())
                .phone(passenger.getPhone())
                .passportNumber(passenger.getPassportNumber())
                .seatInstanceId(passenger.getSeatInstanceId())
                .seatNumber(passenger.getSeatNumber())
                .build();
    }

    public static BookingResponse toResponse(
            Booking booking,
            FlightInstanceResponse flightInstance,
            FareResponse fare
    ) {
        if(booking == null) return null;

        List<BookingPassengerResponse> passengerResponses = booking.getPassengers()
                .stream()
                .map(BookingMapper::toBookingPassengerResponse)
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .flightInstance(flightInstance)
                .fare(fare)
                .cabinClass(booking.getCabinClass())
                .passengerCount(booking.getPassengerCount())
                .baseFare(booking.getBaseFare())
                .taxAmount(booking.getTaxAmount())
                .serviceFee(booking.getServiceFee())
                .totalAmount(booking.getTotalAmount())
                .currency(booking.getCurrency())
                .bookingStatus(booking.getBookingStatus())
                .paymentStatus(booking.getPaymentStatus())
                .expiresAt(booking.getExpiresAt())
                .isActive(booking.getIsActive())
                .passengers(passengerResponses)
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

}
