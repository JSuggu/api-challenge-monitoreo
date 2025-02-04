package com.api.modules.plant;

import com.api.modules.sensor.Sensor;
import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantResponseDTO {
    private String uuid;
    private String name;
    private String country;
    private List<Sensor> sensors;
}
