package com.vaishnav.airlinq.seat.controller;

import com.vaishnav.airlinq.seat.service.FlightInstanceCabinService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.FlightInstanceCabinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight-inst-cabin")
@RequiredArgsConstructor
public class FlightInstanceCabinController {
    private final FlightInstanceCabinService flightInstanceCabinService;

    @GetMapping("/{flightInstanceId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightInstanceCabinResponse> getFlightInstanceCabin(
            @PathVariable Long flightInstanceId
    ) {
        return flightInstanceCabinService.getCabinsByFlightInstanceId(flightInstanceId);
    }

    @GetMapping("/flightInstCabin")
    @ResponseStatus(HttpStatus.OK)
    public FlightInstanceCabinResponse getFlightInsCabin(
            @RequestParam Long flightInstanceId,
            @RequestParam CabinClass cabinClass
    ) throws Exception {
        return flightInstanceCabinService.getCabinByFlightInstanceIdAndCabinClass(
                flightInstanceId, cabinClass);
    }

    @PutMapping("/increase")
    public ResponseEntity<?> increaseFlightInstanceCabinSeat(
            @RequestParam Long flightInstanceId,
            @RequestParam CabinClass cabinClass
    ) {
        try {
            flightInstanceCabinService.increaseBookedSeats(
                    flightInstanceId, cabinClass
            );
            ApiResponse apiResponse = new ApiResponse(
                    "Booked Seat Increased successfully", true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(
                    "Failed to Book Seat", true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/release")
    public ResponseEntity<?> releaseFlightInstanceCabinSeat(
            @RequestParam Long flightInstanceId,
            @RequestParam CabinClass cabinClass
    ) {
        try {
            flightInstanceCabinService.increaseBookedSeats(
                    flightInstanceId, cabinClass
            );
            ApiResponse apiResponse = new ApiResponse(
                    "Booked Seat Released successfully", true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(
                    "Failed to release Book Seat", true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
