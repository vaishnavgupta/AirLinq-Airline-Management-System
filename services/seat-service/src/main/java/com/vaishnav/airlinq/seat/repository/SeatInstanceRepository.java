package com.vaishnav.airlinq.seat.repository;

import com.vaishnav.airlinq.seat.model.SeatInstance;
import com.vaishnav.enums.CabinClass;
import com.vaishnav.enums.SeatInstanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatInstanceRepository extends JpaRepository<SeatInstance, Long> {

    boolean existsByFlightInstanceId(Long flightInstanceId);

    List<SeatInstance> findByFlightInstanceId(Long flightInstanceId);

    List<SeatInstance> findByFlightInstanceIdAndStatus(
            Long flightInstanceId, SeatInstanceStatus status
    );

    List<SeatInstance> findByFlightInstanceIdAndCabinClassAndStatus(
            Long flightInstanceId,
            CabinClass cabinClass,
            SeatInstanceStatus status
    );

    Optional<SeatInstance> findByFlightInstanceIdAndSeatNumber(
            Long flightInstanceId,
            String seatNumber
    );

    boolean existsByFlightInstanceIdAndSeatId(
            Long flightInstanceId,
            Long seatId
    );

    List<SeatInstance> findByStatusAndLockedUntilBefore(
            SeatInstanceStatus status,
            Instant lockedUntil
    );

}
