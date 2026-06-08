package com.vaishnav.airlinq.flight_ops.repository;

import com.vaishnav.airlinq.flight_ops.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
        SELECT f FROM Flight f
        WHERE f.airlineId = :airlineId AND
        (:arrivalAirportId IS NULL OR f.arrivalAirportId = :arrivalAirportId) AND
        (:departureAirportId IS NULL OR f.departureAirportId = :departureAirportId)
    """)
    Page<Flight> getFlightsByAirportIds(
            @Param("airlineId") Long airlineId,
            @Param("arrivalAirportId") Long arrivalAirportId,
            @Param("departureAirportId") Long departureAirportId,
            Pageable pageable
    );

    boolean existsByFlightNumber(String flightNumber);

    Optional<Flight> findByAirlineIdAndId(Long airlineId, Long id);
}
