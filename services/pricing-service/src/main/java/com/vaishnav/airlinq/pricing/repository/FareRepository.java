package com.vaishnav.airlinq.pricing.repository;

import com.vaishnav.airlinq.pricing.model.Fare;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.FareStatus;
import com.vaishnav.enums.FareType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {

    @Query("""
    SELECT f FROM Fare f
    WHERE
    (:airlineId IS NULL OR f.airlineId = :airlineId) AND
    (:flightId IS NULL OR f.flightId = :flightId) AND
    (:flightInstanceId IS NULL OR f.flightInstanceId = :flightInstanceId) AND
    (:cabinClass IS NULL OR f.cabinClass = :cabinClass) AND
    (:fareType IS NULL OR f.fareType = :fareType) AND
    (:status IS NULL OR f.status = :status)
    """)
    Page<Fare> searchFare(
            @Param("airlineId") Long airlineId,
            @Param("flightId") Long flightId,
            @Param("flightInstanceId") Long flightInstanceId,
            @Param("cabinClass") CabinClass cabinClass,
            @Param("fareType") FareType fareType,
            @Param("status") FareStatus status,
            Pageable pageable
    );

    @Query("""
    SELECT f FROM Fare f
    WHERE f.flightId = :flightId ORDER BY f.totalFare ASC LIMIT 1
    """)
    Optional<Fare> getLowestFareByFlightId(
            @Param("flightId") Long flightId
    );

    List<Fare> findByFlightId(Long flightId);

}
