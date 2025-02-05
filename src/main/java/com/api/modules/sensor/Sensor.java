package com.api.modules.sensor;

import com.api.modules.plant.Plant;
import com.api.modules.sensor_type.SensorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    @Column(nullable = false)
    private Integer reading = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer averageAlerts = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer redAlerts = 0;
    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = true;
    @ManyToOne()
    @JoinColumn(name = "sensor_type_id", nullable = false)
    private SensorType sensorType;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;
}
