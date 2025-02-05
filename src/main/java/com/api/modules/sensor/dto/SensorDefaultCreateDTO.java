package com.api.modules.sensor.dto;

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
    String plantUuid;
}
