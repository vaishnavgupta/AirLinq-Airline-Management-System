package com.vaishnav.airlinq.flight_ops.controller;

import com.vaishnav.airlinq.flight_ops.service.FlightScheduleService;
import com.vaishnav.payload.request.FlightScheduleRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.FlightScheduleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightScheduleResponse createFlightSchedule(
            @Valid @RequestBody FlightScheduleRequest flightScheduleRequest,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception{
        return flightScheduleService.createFlightSchedule(
                userId,
                flightScheduleRequest
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightScheduleResponse getFlightSchedule(
            @PathVariable Long id
    ) throws Exception {
        return flightScheduleService.getFlightScheduleById(id);
    }

    @GetMapping("/airline")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightScheduleResponse> getAirlineSchedule(
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return flightScheduleService.getFlightScheduleByAirline(userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightScheduleResponse updateFlightSchedule(
            @PathVariable Long id,
            @RequestBody FlightScheduleRequest flightScheduleRequest
    ) throws Exception {
        return flightScheduleService.updateFlightSchedule(
                id,
                flightScheduleRequest
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlightSchedule(
            @PathVariable Long id
    ) {
        try {
            flightScheduleService.deleteFlightSchedule(id);
            ApiResponse apiResponse = new ApiResponse("Flight Schedule deleted successfully", true);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Failed to delete Flight Schedule", false);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
