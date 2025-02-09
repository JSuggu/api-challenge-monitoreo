package com.api.modules.sensor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDefaultCreateDTO {
    @NotBlank(message = "Plant uuid is mandatory")
    @Schema(description = "Uuid of the plant")
    String plantUuid;
}
