package com.api.modules.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorCreateDTO {
    @NotBlank
    String plantUuid;
    @Min(0)
    @Max(10000)
    @NotNull(message = "Reading cant be null")
    private Integer reading;
    @Min(0)
    @Max(10000)
    @NotNull(message = "Average alerts cant be null")
    private Integer averageAlerts;
    @Min(0)
    @Max(10000)
    @NotNull(message = "Red Alerts cant be null")
    private Integer redAlerts;
    @NotNull(message = "Reading cant be null")
    private Boolean disabled;
    @NotBlank(message = "Sensor type name cant be empty")
    private String sensorTypeName;
}
