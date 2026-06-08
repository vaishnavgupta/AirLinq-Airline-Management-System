package com.vaishnav.airlinq.airline.model;

import com.vaishnav.embeddable.Support;
import com.vaishnav.enums.AirlineStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String iataCode;

    @Column(unique = true, nullable = false)
    private String icaoCode;

    @Column(nullable = false)
    private String name;

    private String alias;

    private String logoUrl;

    private String website;

    @Column(nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private AirlineStatus status = AirlineStatus.ACTIVE;

    private String alliance;

    private Long headquarterCityId;

    @Embedded
    private Support support;

    @CreationTimestamp
    @Column(updatable = false,  nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
