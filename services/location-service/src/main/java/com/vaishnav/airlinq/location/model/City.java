package com.vaishnav.airlinq.location.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cityCode;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private String countryName;

    @Size(max = 10)
    private String regionCode;

    @Column(length = 50)
    private String timeZoneId;
}
