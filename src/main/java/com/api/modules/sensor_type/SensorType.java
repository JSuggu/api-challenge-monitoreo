package com.api.modules.sensor_type;

import com.api.modules.sensor.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "sensorType")
    private List<Sensor> sensors;
}
