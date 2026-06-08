package com.vaishnav.airlinq.seat.repository;

import com.vaishnav.airlinq.seat.model.SeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatMapRepository extends JpaRepository<SeatMap, Long> {
    List<SeatMap> findByAirlineIdAndIsActiveTrue(Long airlineId);

    List<SeatMap> findByAircraftIdAndIsActiveTrue(Long airlineId);

    Optional<SeatMap> findByIdAndAirlineId(Long id, Long airlineId);

    Optional<SeatMap> findFirstByAircraftIdAndIsActiveTrue(Long aircraftId);
}
