package com.vaishnav.airlinq.location.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaishnav.embeddable.Address;
import com.vaishnav.embeddable.GeoCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 3)
    private String iataCode;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private GeoCode geoCode;

    @Column(length = 50)
    private String timeZoneId;

    @ManyToOne
    @JsonIgnore
    private City city;

    @JsonIgnore
    @Transient
    public String getDetailedName() {
        if(city != null && city.getCityCode() != null) {
            return name.toUpperCase() + " - " +  city.getCityCode();
        }
        return name.toUpperCase();
    }

}
