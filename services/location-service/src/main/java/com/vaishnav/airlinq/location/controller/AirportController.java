package com.vaishnav.airlinq.location.controller;

import com.vaishnav.airlinq.location.service.AirportService;
import com.vaishnav.payload.request.AirportRequest;
import com.vaishnav.payload.response.AirportResponse;
import com.vaishnav.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirportResponse createAirport(@Valid @RequestBody AirportRequest airportRequest) {
        return airportService.createAirport(airportRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AirportResponse getAirport(@PathVariable Long id) {
        return airportService.getAirportById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AirportResponse> getAllAirports() {
        return airportService.getAllAirports();
    }

    @PutMapping("/{id}")
    public AirportResponse updateAirport(@PathVariable Long id, @Valid @RequestBody AirportRequest airportRequest) {
        return airportService.updateAirport(id, airportRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAirport(@PathVariable Long id) {
        try {
            airportService.deleteAirportById(id);
            return ResponseEntity.ok(new ApiResponse("Airport deleted successfully!", true));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to delete airport " + ex.getMessage(), false));
        }
    }

    @GetMapping("/cityId/{cityId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AirportResponse> getAirportsByCityId(@PathVariable Long cityId) {
        return airportService.getAirportsByCityId(cityId);
    }

}
