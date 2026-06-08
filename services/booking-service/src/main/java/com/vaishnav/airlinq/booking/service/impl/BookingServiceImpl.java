package com.vaishnav.airlinq.booking.service.impl;

import com.vaishnav.airlinq.booking.client.FlightOpsServiceClient;
import com.vaishnav.airlinq.booking.client.PricingServiceClient;
import com.vaishnav.airlinq.booking.client.SeatServiceClient;
import com.vaishnav.airlinq.booking.mapper.BookingMapper;
import com.vaishnav.airlinq.booking.model.Booking;
import com.vaishnav.airlinq.booking.model.BookingPassenger;
import com.vaishnav.airlinq.booking.repository.BookingRepository;
import com.vaishnav.airlinq.booking.service.BookingService;
import com.vaishnav.enums.BookingStatus;
import com.vaishnav.enums.FlightStatus;
import com.vaishnav.enums.PaymentStatus;
import com.vaishnav.payload.request.*;
import com.vaishnav.payload.response.BookingResponse;
import com.vaishnav.payload.response.FareResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final FlightOpsServiceClient flightOpsServiceClient;
    private final SeatServiceClient seatServiceClient;
    private final PricingServiceClient pricingServiceClient;
    @Override
    @Transactional
    public BookingResponse createBooking(Long userId, BookingRequest bookingRequest) throws Exception {
        if (bookingRequest.getPassengers() == null || bookingRequest.getPassengers().isEmpty()) {
            throw new Exception("At least one passenger is required");
        }

        FareResponse fare = getFareDetails(bookingRequest.getFareId());

        int passengerCount = bookingRequest.getPassengers().size();

        if (
                fare.getIsActive() == false ||
                        !fare.getFlightInstanceResponse().getId().equals(bookingRequest.getFlightInstanceId()) ||
                        !fare.getCabinClass().equals(bookingRequest.getCabinClass())
        ) {
            throw new Exception("Fare details does not match with provided details");
        }

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstanceById(bookingRequest.getFlightInstanceId());
        if (flightInstanceResponse == null || flightInstanceResponse.getId() == null) {
            throw new Exception("Flight Instance does not exist");
        }
        if(flightInstanceResponse.getAvailableSeats() <  passengerCount) {
            throw new Exception("Passenger count is more than available seats");
        }
        if(flightInstanceResponse.getStatus() != FlightStatus.SCHEDULED) {
            throw new Exception("Fight Instance is not in SCHEDULED state");
        }

        for (BookingPassengerRequest passenger : bookingRequest.getPassengers()) {
            if (passenger.getSeatInstanceId() == null) {
                throw new Exception("Seat instance id is required for every passenger");
            }
        }


        BigDecimal baseFare = fare.getBaseFare().multiply(BigDecimal.valueOf(passengerCount));
        BigDecimal taxAmount = (fare.getTaxAmount() != null)
                ? fare.getTaxAmount().multiply(BigDecimal.valueOf(passengerCount))
                : BigDecimal.ZERO;
        BigDecimal serviceFee = (fare.getServiceFee() != null)
                ? fare.getServiceFee()
                : BigDecimal.ZERO;
        BigDecimal totalAmount = baseFare.add(taxAmount).add(serviceFee);

        Booking booking = Booking.builder()
                .bookingReference(generateBookingReference())
                .userId(userId)
                .flightInstanceId(bookingRequest.getFlightInstanceId())
                .fareId(fare.getId())
                .cabinClass(bookingRequest.getCabinClass())
                .passengerCount(passengerCount)
                .baseFare(baseFare)
                .taxAmount(taxAmount)
                .serviceFee(serviceFee)
                .totalAmount(totalAmount)
                .currency(fare.getCurrency())
                .bookingStatus(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .isActive(true)
                .build();

        List<BookingPassenger> bookingPassengers = bookingRequest.getPassengers()
                .stream()
                .map(BookingMapper::toBookingPassenger)
                .peek(passenger -> passenger.setBooking(booking))
                .toList();

        booking.getPassengers().addAll(bookingPassengers);

        Booking savedBooking = bookingRepository.save(booking);

        // Seat Service - Hold Booking Seats
        for(BookingPassenger passenger : savedBooking.getPassengers()) {
           SeatHoldRequest seatHoldRequest = SeatHoldRequest.builder()
                   .bookingId(savedBooking.getId())
                   .passengerId(passenger.getId())
                   .holdMinutes(12)
                   .build();
           seatServiceClient.holdSeat(passenger.getSeatInstanceId(), seatHoldRequest);
        }

        //TODO - payment-service (make payment - CONFIRM Seats)

        return toResponse(savedBooking, flightInstanceResponse, fare);
    }

    @Override
    public BookingResponse getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking does not exists with id"));
        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstanceById(booking.getFlightInstanceId());
        FareResponse fareResponse = pricingServiceClient.getFareById(booking.getFareId());
        return toResponse(booking, flightInstanceResponse, fareResponse);
    }

    @Override
    public BookingResponse getBookingByReference(String bookingReference) throws Exception {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new Exception("Booking does not exists with given reference"));

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstanceById(booking.getFlightInstanceId());
        FareResponse fareResponse = pricingServiceClient.getFareById(booking.getFareId());
        return toResponse(booking, flightInstanceResponse, fareResponse);
    }

    @Override
    public Page<BookingResponse> getBookingByUserId(Long userId, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findByUserId(userId, pageable);
        return convertPageToBookingResponse(bookings);
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(Long id, BookingConfirmRequest request) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking does not exists with id"));

        if (!booking.getBookingStatus().equals(BookingStatus.PENDING)) {
            throw new Exception("Only PENDING bookings can be confirmed");
        }

        if (booking.getExpiresAt() != null && booking.getExpiresAt().isBefore(Instant.now())) {
            booking.setBookingStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new Exception("Booking session is expired");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setExpiresAt(null);

        // Seat - Service == Book Held Seats
        for(BookingPassenger passenger : booking.getPassengers()) {
            SeatBookRequest seatBookRequest = SeatBookRequest.builder()
                    .bookingId(booking.getId())
                    .passengerId(passenger.getId())
                    .build();
            seatServiceClient.bookSeat(passenger.getSeatInstanceId(), seatBookRequest);
        }

        // TODO - Store payment id

        booking = bookingRepository.save(booking);

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstanceById(booking.getFlightInstanceId());
        FareResponse fareResponse = pricingServiceClient.getFareById(booking.getFareId());
        return toResponse(booking, flightInstanceResponse, fareResponse);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id, Long userId) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking does not exists with id"));

        if (!booking.getUserId().equals(userId)) {
            throw new Exception("User does not belong to this Booking");
        }

        FlightInstanceResponse flightInstanceResponse = flightOpsServiceClient.getFlightInstanceById(booking.getFlightInstanceId());
        FareResponse fareResponse = pricingServiceClient.getFareById(booking.getFareId());

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            return toResponse(booking, flightInstanceResponse, fareResponse);
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setIsActive(false);

        if (booking.getPaymentStatus() == PaymentStatus.PAID) {
            booking.setPaymentStatus(PaymentStatus.REFUNDED);
            // todo -> Refund API --> Not Implemented (Skipped)
        }

        for(BookingPassenger passenger : booking.getPassengers()) {
            seatServiceClient.releaseSeat(passenger.getSeatInstanceId());
        }

        booking = bookingRepository.save(booking);

        return toResponse(booking, flightInstanceResponse, fareResponse);
    }

    @Override
    @Transactional
    public void expirePendingBookings() throws Exception {
        List<Booking> bookings = bookingRepository.findByBookingStatusAndExpiresAtBefore(
                BookingStatus.EXPIRED,
                Instant.now().plus(1, ChronoUnit.MINUTES)
        );
        for (Booking booking : bookings) {
            booking.setBookingStatus(BookingStatus.EXPIRED);
            booking.setIsActive(false);
            for(BookingPassenger passenger : booking.getPassengers()) {
                seatServiceClient.releaseSeat(passenger.getSeatInstanceId());
            }
        }

        bookingRepository.saveAll(bookings);
    }

    private BookingResponse toResponse(Booking booking, FlightInstanceResponse flightInstance, FareResponse fare) {
        return BookingMapper.toResponse(booking, flightInstance, fare);
    }

    private FareResponse getFareDetails(Long fareId) throws Exception {
        FareResponse fareResponse = pricingServiceClient.getFareById(fareId);
        if (fareResponse == null || !Objects.equals(fareResponse.getId(), fareId)) {
            throw new Exception("Fare does not exist with fareId");
        }
        return fareResponse;
    }

    private String generateBookingReference() {
        return "ALQ-" + UUID.randomUUID()
                .toString()
                .substring(0, 10)
                .toUpperCase();
    }

    private Page<BookingResponse> convertPageToBookingResponse(
            Page<Booking> page
    ) {

        Map<Long, FlightInstanceResponse> flightInstanceCache = new HashMap<>();
        Map<Long, FareResponse> fareCache = new HashMap<>();

        List<BookingResponse> responseList = page.getContent()
                .stream()
                .map(booking -> {

                    FlightInstanceResponse flightInstance = flightInstanceCache.computeIfAbsent(
                            booking.getFlightInstanceId(),
                            id -> {
                                try {
                                    return flightOpsServiceClient.getFlightInstanceById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch flightInstance: " + id, e);
                                }
                            }
                    );

                    FareResponse fare = fareCache.computeIfAbsent(
                            booking.getFareId(),
                            id -> {
                                try {
                                    return pricingServiceClient.getFareById(id);
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to fetch fare: " + id, e);
                                }
                            }
                    );
                    return toResponse(
                            booking,
                            flightInstance,
                            fare
                    );
                })
                .toList();


        return new PageImpl<>(
                responseList,
                page.getPageable(),
                page.getTotalElements()
        );
    }

}
