package com.api.modules.sensor_type;

import com.api.modules.sensor.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sensors_types")
public class SensorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id of the sensor type")
    private Long id;
    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the sensor type")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "sensorType")
    private List<Sensor> sensors;
}
