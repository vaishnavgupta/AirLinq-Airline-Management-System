package com.vaishnav.airlinq.airline.controller;

import com.vaishnav.airlinq.airline.service.AirlineService;
import com.vaishnav.enums.AirlineStatus;
import com.vaishnav.payload.request.AirlineRequest;
import com.vaishnav.payload.response.AirlineDropDownItem;
import com.vaishnav.payload.response.AirlineResponse;
import com.vaishnav.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airline")
@RequiredArgsConstructor
public class AirlineController {

    private final AirlineService airlineService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineResponse createAirline(
            @Valid @RequestBody AirlineRequest airlineRequest,
            @RequestHeader("X-User-Id") Long ownerId
    ) throws Exception {
        return airlineService.createAirline(airlineRequest, ownerId);
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public AirlineResponse getAirlineByOwner(@RequestHeader("X-User-Id") Long ownerId) throws Exception {
        System.out.println("Owner Id : ***********   " + ownerId);
        return airlineService.getAirlineByOwner(ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AirlineResponse getAirlineById(@PathVariable Long id) throws Exception {
        return airlineService.getAirlineById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<AirlineResponse> getAllAirlines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toLowerCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return airlineService.getAllAirlines(pageable);
    }

    @GetMapping("/dropdown")
    @ResponseStatus(HttpStatus.OK)
    public List<AirlineDropDownItem> getAirlineForDropdown() {
        return airlineService.getAirlineDropdown();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public AirlineResponse updateAirline(
            @Valid @RequestBody AirlineRequest airlineRequest,
            @RequestHeader("X-User-Id") Long ownerId
    ) throws Exception {
        return airlineService.updateAirline(airlineRequest, ownerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long ownerId
    ) {
        try {
            airlineService.deleteAirline(id, ownerId);
            ApiResponse apiResponse = new ApiResponse("Airline deleted successfully", true);
            return ResponseEntity.ok(apiResponse);
        }
        catch (Exception ex) {
            ApiResponse apiResponse = new ApiResponse("Failed to delete airline", false);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<AirlineResponse> approveAirline(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(airlineService.changeStatusByAdmin(id, AirlineStatus.ACTIVE));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<AirlineResponse> suspendAirline(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(airlineService.changeStatusByAdmin(id, AirlineStatus.INACTIVE));
    }

    @PostMapping("/{id}/ban")
    public ResponseEntity<AirlineResponse> banAirline(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(airlineService.changeStatusByAdmin(id, AirlineStatus.BANNED));
    }

}
