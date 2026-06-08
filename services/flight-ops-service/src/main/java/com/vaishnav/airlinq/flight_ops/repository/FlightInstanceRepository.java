package com.vaishnav.airlinq.flight_ops.repository;

import com.vaishnav.airlinq.flight_ops.model.FlightInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long> {

    @Query("""
    SELECT fi FROM FlightInstance fi
    WHERE
    (:airlineId IS NULL OR fi.airlineId=:airlineId) AND
    (:departureAirportId IS NULL OR fi.departureAirportId=:departureAirportId) AND
    (:arrivalAirportId IS NULL OR fi.arrivalAirportId=:arrivalAirportId) AND
    (:flightId IS NULL OR fi.flight.id=:flightId) AND
    (:onDate IS NULL OR fi.departureTime=:onDate)
    """)
    Page<FlightInstance> search(
            @Param("airlineId") Long airlineId,
            @Param("departureAirportId") Long departureAirportId,
            @Param("arrivalAirportId") Long arrivalAirportId,
            @Param("flightId") Long flightId,
            @Param("onDate") LocalDateTime onDate,
            Pageable pageable
    );

    List<FlightInstance> findByAirlineId(Long airlineId);

}
