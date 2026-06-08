package com.vaishnav.airlinq.pricing.model;

import com.vaishnav.enums.BaggageUnit;
import com.vaishnav.enums.CabinClass;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BaggagePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long airlineId;

    private Long fareId;

    private Long flightId;

    private Long flightInstanceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabinClass;

    @Column(nullable = false)
    private Integer cabinBagAllowance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaggageUnit cabinBagUnit;

    private BigDecimal cabinBagMaxWeight;

    @Column(nullable = false)
    private Integer checkedBagAllowance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaggageUnit checkedBagUnit;

    private BigDecimal checkedBagMaxWeight;

    private BigDecimal extraBagFee;

    private BigDecimal overweightFee;

    private String policyDescription;

    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
