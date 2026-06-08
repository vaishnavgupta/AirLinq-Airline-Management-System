package com.vaishnav.airlinq.flight_ops.controller;

import com.vaishnav.airlinq.flight_ops.service.FlightInstanceService;
import com.vaishnav.payload.request.FlightInstanceRequest;
import com.vaishnav.payload.request.FlightInstanceSearchRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.FlightInstanceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flight-instance")
@RequiredArgsConstructor
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightInstanceResponse createFlightInstance(
            @Valid @RequestBody FlightInstanceRequest flightInstanceRequest,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return flightInstanceService.createFlightInstance(
                userId, flightInstanceRequest
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightInstanceResponse getFlightInstanceById(
            @PathVariable Long id
    ) throws Exception {
        return flightInstanceService.getFlightInstanceById(id);
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<FlightInstanceResponse> searchFlightInstance(
            @RequestBody FlightInstanceSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return flightInstanceService.getByAirlineId(
                request.getAirlineId(),
                request.getDepartureAirportId(),
                request.getArrivalAirportId(),
                request.getFlightId(),
                request.getOnDate(),
                pageable
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightInstanceResponse updateFlightInstance(
            @PathVariable Long id,
            @RequestBody FlightInstanceRequest flightInstanceRequest
    ) throws Exception {
        return flightInstanceService.updateFlightInstance(
                id, flightInstanceRequest
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlightInstance(
            @PathVariable Long id
    ) {
        try {
            flightInstanceService.deleteFlightInstance(id);
            ApiResponse apiResponse = new ApiResponse(
                    "Flight Instance deleted successfully",
                    true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception ex) {
            ApiResponse apiResponse = new ApiResponse(
                    "Failed to delete Flight Instance",
                    false
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
