package com.api.modules.sensor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorCreateDTO {
    @NotBlank(message = "Plant uuid is mandatory")
    @Schema(description = "Uuid of the plant")
    String plantUuid;
    @NotBlank(message = "Sensor type name cant be empty")
    @Schema(description = "Type name of the sensor")
    private String sensorTypeName;
}
