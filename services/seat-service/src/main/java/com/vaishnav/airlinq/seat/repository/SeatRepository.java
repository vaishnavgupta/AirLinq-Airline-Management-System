package com.vaishnav.airlinq.seat.repository;

import com.vaishnav.airlinq.seat.model.Seat;
import com.vaishnav.enums.CabinClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findBySeatMapIdAndIsActiveTrue(Long id);

    List<Seat> findBySeatMapIdAndCabinClassAndIsActiveTrue(Long id, CabinClass  cabinClass);

    Optional<Seat> findBySeatMapIdAndSeatNumber(Long seatMapId, String seatNumber);

    Boolean existsBySeatMapIdAndSeatNumber(Long seatMapId, String seatNumber);

}
