package com.api.modules.plant;

import com.api.modules.sensor.Sensor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantResponseDTO {
    @Schema(description = "Uuid of the plant.")
    private String uuid;
    @Schema(description = "Name of the plant.")
    private String name;
    @Schema(description = "Country of the plant.")
    private String country;
    @Schema(description = "List of sensors in the plant.")
    private List<Sensor> sensors;
}
