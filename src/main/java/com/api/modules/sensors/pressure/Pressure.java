package com.api.modules.sensors.pressure;

import com.api.modules.plant.Plant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pressures")
public class Pressure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer reading = 0;
    @Column(nullable = false)
    private Integer averageAlerts = 0;
    @Column(nullable = false)
    private Integer redAlerts = 0;
    @Column(nullable = false)
    private Boolean disabled = false;
}
