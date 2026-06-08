package com.vaishnav.airlinq.flight_ops.controller;

import com.vaishnav.airlinq.flight_ops.service.FlightService;
import com.vaishnav.enums.FlightStatus;
import com.vaishnav.payload.request.FlightRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.FlightResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(
            @Valid @RequestBody  FlightRequest request,
            @RequestHeader("X-User-Id") Long ownerId
    ) throws Exception {
        return flightService.createFlight(ownerId, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse getFlightById(@PathVariable Long id) throws Exception {
        return flightService.getFlightById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse updateFlight(
            @RequestBody FlightRequest request,
            @PathVariable Long id
    ) throws Exception {
        return flightService.updateFlight(id, request);
    }

    @GetMapping("/airline")
    @ResponseStatus(HttpStatus.OK)
    public Page<FlightResponse> searchByAirline(
            @RequestHeader("X-User-Id") Long ownerId,
            @RequestParam(required = false) Long arrivalAirportId,
            @RequestParam(required = false) Long departureAirportId,
            Pageable pageable
    ) {
        return flightService.getFlightsByAirlineId(
                ownerId,        //Actually airlineId is passed
                departureAirportId,
                arrivalAirportId,
                pageable
        );
    }

    @PostMapping("/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse changeFlightStatus(
            @PathVariable Long id,
            @RequestParam(defaultValue = "SCHEDULED") FlightStatus status
            ) throws Exception {
        return flightService.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlight(
            @RequestHeader("X-User-Id") Long ownerId,
            @PathVariable Long id
    )  {
        try {
            flightService.deleteFlight(
                    ownerId,        //Actually airlineId is passed
                    id
            );
            ApiResponse apiResponse = new ApiResponse("Flight deleted successfully", true);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Failed to delete flight", false);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
