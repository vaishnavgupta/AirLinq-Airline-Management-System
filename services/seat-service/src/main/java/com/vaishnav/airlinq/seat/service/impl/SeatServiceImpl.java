package com.vaishnav.airlinq.seat.service.impl;

import com.vaishnav.airlinq.seat.mapper.SeatMapper;
import com.vaishnav.airlinq.seat.model.Seat;
import com.vaishnav.airlinq.seat.model.SeatMap;
import com.vaishnav.airlinq.seat.repository.SeatMapRepository;
import com.vaishnav.airlinq.seat.repository.SeatRepository;
import com.vaishnav.airlinq.seat.service.SeatService;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.payload.request.SeatRequest;
import com.vaishnav.payload.response.SeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapRepository  seatMapRepository;

    @Override
    public SeatResponse createSeat(SeatRequest seatRequest) throws Exception {
        if (seatRepository.existsBySeatMapIdAndSeatNumber(seatRequest.getSeatMapId(), seatRequest.getSeatNumber())) {
            throw new Exception("Seat with map id and number already exists");
        }
        SeatMap seatMap = seatMapRepository.findById(seatRequest.getSeatMapId())
                .orElseThrow(() -> new Exception("Seat Map with id " + seatRequest.getSeatMapId() + " does not exist"));

        Seat seat = SeatMapper.toSeat(seatRequest,  seatMap);
        seat = seatRepository.save(seat);

        return SeatMapper.toResponse(seat);
    }

    @Override
    public SeatResponse getSeatById(Long seatId) throws Exception {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception("Seat with id does not exists"));
        return SeatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponse> getSeatsBySeatMapId(Long seatMapId) {
        return seatRepository.findBySeatMapIdAndIsActiveTrue(seatMapId)
                .stream()
                .map(SeatMapper::toResponse)
                .toList();
    }

    @Override
    public List<SeatResponse> getSeatsBySeatMapIdAndCabinClass(Long seatMapId, CabinClass cabinClass) {
        return seatRepository.findBySeatMapIdAndCabinClassAndIsActiveTrue(
                seatMapId, cabinClass
        ).stream()
                .map(SeatMapper::toResponse)
                .toList();
    }

    @Override
    public SeatResponse updateSeat(Long id, SeatRequest request) throws Exception {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat with id does not exists"));
        SeatMapper.updateEntity(seat, request);
        seat = seatRepository.save(seat);
        return SeatMapper.toResponse(seat);
    }

    @Override
    public void deleteSeat(Long id) throws Exception {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat with id does not exists"));
        seatRepository.delete(seat);
    }
}
