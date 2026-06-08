package com.vaishnav.airlinq.seat.controller;

import com.vaishnav.airlinq.seat.service.SeatService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.SeatRequest;
import com.vaishnav.payload.response.ApiResponse;
import com.vaishnav.payload.response.SeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatResponse createSeat(
            @Valid @RequestBody SeatRequest seatRequest
    ) throws Exception {
        return seatService.createSeat(seatRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeatResponse getSeatById(
            @PathVariable Long id
    ) throws Exception {
        return seatService.getSeatById(id);
    }

    @GetMapping("/seatMap/{seatMapId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatResponse> getSeatBySeatMapId(@PathVariable Long seatMapId) throws Exception {
        return seatService.getSeatsBySeatMapId(seatMapId);
    }

    @GetMapping("/seatMapCabin")
    @ResponseStatus(HttpStatus.OK)
    public List<SeatResponse> getSeatBySeatMapCabin(
            @RequestParam Long  seatMapId,
            @RequestParam CabinClass cabinClass
    ) {
        return seatService.getSeatsBySeatMapIdAndCabinClass(seatMapId, cabinClass);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeatResponse updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequest seatRequest
    ) throws Exception {
        return seatService.updateSeat(id, seatRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSeat(
            @PathVariable Long id
    ) {
        try{
            seatService.deleteSeat(id);
            ApiResponse apiResponse = new ApiResponse(
                    "Seat deleted successfully", true
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(
                    "Failed to delete seat", false
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
