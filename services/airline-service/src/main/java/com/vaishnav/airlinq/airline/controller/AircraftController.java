package com.vaishnav.airlinq.airline.controller;

import com.vaishnav.airlinq.airline.service.AircraftService;
import com.vaishnav.payload.request.AircraftRequest;
import com.vaishnav.payload.response.AircraftResponse;
import com.vaishnav.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
@RequiredArgsConstructor
public class AircraftController {
    private final AircraftService aircraftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftResponse createAircraft(
            @Valid @RequestBody AircraftRequest aircraftRequest,
            @RequestHeader("X-User-Id") Long ownerId
    ) throws Exception {
        return aircraftService.createAircraft(aircraftRequest, ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftResponse getAircraftById(
            @PathVariable Long id
    ) throws Exception {
        return aircraftService.getById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AircraftResponse> getAllAircraftByOwnerId(
            @RequestHeader("X-User-Id")  Long ownerId
    ) throws Exception {
        return aircraftService.listAllAircraftByOwner(ownerId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftResponse updateAircraft(
            @PathVariable Long id,
            @Valid @RequestBody AircraftRequest aircraftRequest,
            @RequestHeader("X-User-Id")  Long ownerId
    ) throws Exception {
        return aircraftService.updateAircraft(id, aircraftRequest, ownerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAircraft(
            @PathVariable Long id,
            @RequestHeader("X-User-Id")  Long ownerId
    ) {
        try {
            aircraftService.deleteAircraft(id, ownerId);
            ApiResponse apiResponse = new ApiResponse(
                    "Aircraft deleted successfully",
                    true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception ex) {
            ApiResponse apiResponse = new ApiResponse(
                    "Failed to delete Aircraft",
                    false
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
