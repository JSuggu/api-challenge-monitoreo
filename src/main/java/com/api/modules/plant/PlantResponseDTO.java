package com.api.modules.plant;

import com.api.modules.sensors.carbonmonoxide.CarbonMonoxide;
import com.api.modules.sensors.energy.Energy;
import com.api.modules.sensors.levels.Levels;
import com.api.modules.sensors.othergases.OtherGases;
import com.api.modules.sensors.pressure.Pressure;
import com.api.modules.sensors.temperature.Temperature;
import com.api.modules.sensors.tension.Tension;
import com.api.modules.sensors.wind.Wind;
import lombok.*;

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
    private Temperature temperature;
    private Pressure pressure;
    private Wind wind;
    private Levels levels;
    private Energy energy;
    private Tension tension;
    private CarbonMonoxide carbonMonoxide;
    private OtherGases otherGases;
}
