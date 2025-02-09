package com.api.modules.sensor;

import com.api.modules.plant.Plant;
import com.api.modules.sensor_type.SensorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Id of the sensor")
    private Long id;
    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "Readings of the sensor")
    private Integer reading = 0;
    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "Average alerts of the sensor")
    private Integer averageAlerts = 0;
    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "Red alerts of the sensor")
    private Integer redAlerts = 0;
    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "State of the sensor")
    private Boolean enabled = true;
    @ManyToOne()
    @JoinColumn(name = "sensor_type_id", nullable = false)
    @Schema(description = "Type of the sensor")
    private SensorType sensorType;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;
}
