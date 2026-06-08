package com.vaishnav.airlinq.pricing.controller;

import com.vaishnav.airlinq.pricing.model.Fare;
import com.vaishnav.airlinq.pricing.service.FareService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import com.vaishnav.payload.request.FareRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.FareResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fares")
@RequiredArgsConstructor
public class FareController {
    private final FareService fareService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FareResponse createFare(
            @Valid @RequestBody FareRequest request,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return fareService.createFare(userId, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FareResponse getFareById(
            @PathVariable Long id
    ) throws Exception {
        return fareService.getFareById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<FareResponse> searchFare(
            @RequestParam(required = false) Long airlineId,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) Long flightInstanceId,
            @RequestParam(required = false) CabinClass cabinClass,
            @RequestParam(required = false) FareType fareType,
            @RequestParam(required = false) FareStatus status,
            Pageable pageable
    ) {
        return fareService.searchFares(
                airlineId, flightId, flightInstanceId, cabinClass, fareType, status, pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FareResponse updateFare(
            @PathVariable Long id,
            @RequestBody FareRequest request
    ) throws Exception {
        return fareService.updateFare(
                id, request
        );
    }

    @GetMapping("/flight/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<FareResponse> getFareByFlightId(
            @PathVariable Long id
    ) throws Exception {
        return fareService.getFaresByFlightId(id);
    }

    @GetMapping("/flight/{id}/lowest")
    @ResponseStatus(HttpStatus.OK)
    public FareResponse getFareByFlightIdLowest(
            @PathVariable Long id
    ) throws Exception {
        return fareService.getLowestFareByFlightId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFareById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        try {
            fareService.deleteFare(userId, id);
            ApiResponse response = new ApiResponse(
                    "Fare deleted successfully", true
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex) {
            ApiResponse response = new ApiResponse(
                    "Failed to delete fare", false
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
