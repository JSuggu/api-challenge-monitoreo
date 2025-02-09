package com.api.modules.sensor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorUpdateDTO {
    @NotBlank
    @Schema(description = "Uui of the plant")
    String plantUuid;
    @Min(value = 1, message = "Min value cant be less than 0")
    @Max(value = 10000, message = "Max value cant be greater than 10000")
    @NotNull(message = "Readings cant be null")
    @Schema(description = "Readings of the sensor")
    Integer readings;
    @Min(value = 1, message = "Min value cant be less than 0")
    @Max(value = 10000, message = "Max value cant be greater than 10000")
    @NotNull(message = "Average alerts cant be null")
    @Schema(description = "Average alerts of the sensor")
    Integer averageAlerts;
    @Min(value = 1, message = "Min value cant be less than 0")
    @Max(value = 10000, message = "Max value cant be greater than 10000")
    @NotNull(message = "Red alerts cant be null")
    @Schema(description = "Red alerts of the sensor")
    Integer redAlerts;
    @Schema(description = "State of the sensor")
    Boolean enabled;
}
