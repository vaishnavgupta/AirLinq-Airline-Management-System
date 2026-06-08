package com.vaishnav.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Support {
    private String email;
    private String phone;
    private String hours;
}
