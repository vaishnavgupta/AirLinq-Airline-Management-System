package com.vaishnav.airlinq.seat.repository;

import com.vaishnav.airlinq.seat.model.FlightInstanceCabin;
import com.vaishnav.enums.CabinClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightInstanceCabinRepository extends JpaRepository<FlightInstanceCabin, Long> {
    List<FlightInstanceCabin> findByFlightInstanceIdAndIsActiveTrue(Long flightInstanceId);

    Optional<FlightInstanceCabin> findByFlightInstanceIdAndCabinClassAndIsActiveTrue(
            Long flightInstanceId,
            CabinClass cabinClass
    );

    Optional<FlightInstanceCabin> findByIdAndAirlineId(Long id, Long airlineId);
}
