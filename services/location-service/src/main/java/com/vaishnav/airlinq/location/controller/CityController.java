package com.vaishnav.airlinq.location.controller;

import com.vaishnav.payload.request.CityRequest;
import com.vaishnav.payload.response.CityResponse;
import com.vaishnav.airlinq.location.service.CityService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityResponse createCity( @Valid @RequestBody CityRequest cityRequest) {
        return cityService.createCity(cityRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityResponse getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<CityResponse> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return cityService.getAllCities(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityResponse updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest cityRequest
    ) {
        return cityService.updateCity(id, cityRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCity(id);
            return new ResponseEntity<>(
                    new ApiResponse("City Deleted Successfully", true),
                    HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse("Failed to delete city " + e.getMessage(), false),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<CityResponse> searchCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return cityService.searchCities(keyword, pageable);
    }

    @GetMapping("/country/countryCode")
    @ResponseStatus(HttpStatus.OK)
    public Page<CityResponse> searchCitiesByCountryCode(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam String countryCode
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return cityService.searchCitiesByCountryCode(countryCode.toUpperCase(), pageable);
    }

    @GetMapping("/exists/{cityCode}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkCityExists(
            @PathVariable String cityCode
    ) {
        return cityService.cityExists(cityCode.toUpperCase());
    }

}
