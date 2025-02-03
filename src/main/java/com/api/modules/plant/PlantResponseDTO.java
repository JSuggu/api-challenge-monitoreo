package com.api.modules.plant;

import com.api.modules.sensor.Sensor;
import lombok.*;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantResponseDTO {
    private String uuid = UUID.randomUUID().toString();
    private String name;
    private String country;
    private List<Sensor> sensors;
}
