package com.vaishnav.airlinq.airline.repository;

import com.vaishnav.airlinq.airline.model.Aircraft;
import com.vaishnav.airlinq.airline.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    List<Aircraft> findByAirlineId(Long airlineId);

    boolean existsByCode(String code);

    List<Aircraft> findByAirline(Airline airline);

    Optional<Aircraft> findByIdAndAirline(Long id, Airline airline);
}
