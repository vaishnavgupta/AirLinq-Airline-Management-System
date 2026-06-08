package com.vaishnav.airlinq.location.repository;

import com.vaishnav.airlinq.location.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Airport findByIataCode(String iataCode);

    boolean existsByIataCode(String iataCode);

    List<Airport> findByCityId(Long cityId);

}
