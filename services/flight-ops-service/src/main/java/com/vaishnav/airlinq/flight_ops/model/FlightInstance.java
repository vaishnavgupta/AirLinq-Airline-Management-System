package com.vaishnav.airlinq.flight_ops.model;

import com.vaishnav.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FlightInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long airlineId;

    @ManyToOne
    private Flight flight;

    @Column(nullable = false)
    private Long departureAirportId;

    @Column(nullable = false)
    private Long arrivalAirportId;

    @Column(nullable = false)
    private Long scheduleId;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    private Integer minAdvanceBookingDays;

    private Integer maxAdvanceBookingDays;

    private Boolean isActive = true;

    private String terminal;

    private String gate;


    @Transient
    public String getFormattedDuration() {
        if(departureTime == null || arrivalTime == null){
            return null;
        }
        Duration duration = Duration.between(
                departureTime,
                arrivalTime
        );
        Long hrs = duration.toHours();
        int mins = duration.toMinutesPart() % 60;

        StringBuilder sb = new StringBuilder();
        if(hrs>0) sb.append(hrs).append("h");
        if(mins>0) sb.append(" ").append(mins).append("m");

        return sb.toString().trim();
    }

}
