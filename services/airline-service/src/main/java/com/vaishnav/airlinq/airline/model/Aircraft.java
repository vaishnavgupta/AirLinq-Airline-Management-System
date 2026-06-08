package com.vaishnav.airlinq.airline.model;

import com.vaishnav.enums.AircraftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, length = 50)
    private String manufacturer;

    @Column(nullable = false)
    private Integer seatingCapacity;

    private Integer economySeats = 0;

    private Integer premiumEconomySeats = 0;

    private Integer businessSeats = 0;

    private Integer firstClassSeats = 0;

    private Integer cruisingSpeedKmh;

    private Integer rangeKmh;

    private Integer yearOfManufacturer;

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    @Column(nullable = false, name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private AircraftStatus status = AircraftStatus.ACTIVE;

    private Boolean isAvailable = true;

    @ManyToOne
    private Airline airline;

    private Long currentAirportId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Integer getTotalSeats() {
        return economySeats + premiumEconomySeats + businessSeats + firstClassSeats;
    }

    public Boolean isOperational() {
        return AircraftStatus.ACTIVE.equals(status)
                && isAvailable == true;
    }

    public Boolean requireMaintenance() {
        return nextMaintenanceDate != null
                && nextMaintenanceDate.isBefore(LocalDate.now().plusDays(12));
    }

}
