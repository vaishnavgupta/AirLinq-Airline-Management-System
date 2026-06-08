package com.vaishnav.airlinq.pricing.repository;

import com.vaishnav.airlinq.pricing.model.BaggagePolicy;
import com.vaishnav.enums.CabinClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaggagePolicyRepository extends JpaRepository<BaggagePolicy, Long> {

    @Query("""
    SELECT bp FROM BaggagePolicy bp
    WHERE
    (:airlineId IS NULL OR bp.airlineId = :airlineId) AND
    (:fareId IS NULL OR bp.fareId = :fareId) AND
    (:flightId IS NULL OR bp.flightId = :flightId) AND
    (:flightInstanceId IS NULL OR bp.flightInstanceId = :flightInstanceId) AND
    (:cabinClass IS NULL OR bp.cabinClass = :cabinClass) AND
    bp.isActive = true
    """)
    Page<BaggagePolicy> searchBaggagePolicies(
            @Param("airlineId") Long airlineId,
            @Param("fareId") Long fareId,
            @Param("flightId") Long flightId,
            @Param("flightInstanceId") Long flightInstanceId,
            @Param("cabinClass") CabinClass cabinClass,
            Pageable pageable
    );

    Optional<BaggagePolicy> findByFareIdAndIsActiveTrue(Long fareId);

    Optional<BaggagePolicy> findFirstByFlightInstanceIdAndCabinClassAndIsActiveTrue(
            Long flightInstanceId,
            CabinClass cabinClass
    );

    Optional<BaggagePolicy> findByIdAndAirlineId(Long id, Long airlineId);
}
