package com.vaishnav.airlinq.pricing.controller;

import com.vaishnav.airlinq.pricing.service.BaggagePolicyService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.BaggagePolicyRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.BaggagePolicyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baggage-policies")
@RequiredArgsConstructor
public class BaggagePolicyController {

    private final BaggagePolicyService baggagePolicyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaggagePolicyResponse createBaggagePolicy(
            @Valid @RequestBody BaggagePolicyRequest request,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return baggagePolicyService.createBaggagePolicy(userId, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaggagePolicyResponse getBaggagePolicyById(
            @PathVariable Long id
    ) throws Exception {
        return baggagePolicyService.getBaggagePolicyById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<BaggagePolicyResponse> searchBaggagePolicies(
            @RequestParam(required = false) Long airlineId,
            @RequestParam(required = false) Long fareId,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) Long flightInstanceId,
            @RequestParam(required = false) CabinClass cabinClass,
            Pageable pageable
    ) {
        return baggagePolicyService.searchBaggagePolicies(
                airlineId,
                fareId,
                flightId,
                flightInstanceId,
                cabinClass,
                pageable
        );
    }

    @GetMapping("/fare/{fareId}")
    @ResponseStatus(HttpStatus.OK)
    public BaggagePolicyResponse getPolicyForFare(
            @PathVariable Long fareId
    ) throws Exception {
        return baggagePolicyService.getPolicyForFare(fareId);
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    @ResponseStatus(HttpStatus.OK)
    public BaggagePolicyResponse getPolicyForFlightInstance(
            @PathVariable Long flightInstanceId,
            @RequestParam CabinClass cabinClass
    ) throws Exception {
        return baggagePolicyService.getPolicyForFlightInstance(flightInstanceId, cabinClass);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaggagePolicyResponse updateBaggagePolicy(
            @PathVariable Long id,
            @RequestBody BaggagePolicyRequest request
    ) throws Exception {
        return baggagePolicyService.updateBaggagePolicy(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBaggagePolicy(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        try {
            baggagePolicyService.deleteBaggagePolicy(userId, id);
            ApiResponse response = new ApiResponse("Baggage policy deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponse response = new ApiResponse("Failed to delete baggage policy", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
