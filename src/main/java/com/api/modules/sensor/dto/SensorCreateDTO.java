package com.api.modules.sensor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorCreateDTO {
    @NotBlank(message = "Plant uuid is mandatory")
    String plantUuid;
    @NotBlank(message = "Sensor type name cant be empty")
    private String sensorTypeName;
}
