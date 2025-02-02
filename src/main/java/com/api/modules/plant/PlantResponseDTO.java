package com.api.modules.plant;

import com.api.modules.carbonmonoxide.CarbonMonoxide;
import com.api.modules.energy.Energy;
import com.api.modules.levels.Levels;
import com.api.modules.othergases.OtherGases;
import com.api.modules.pressure.Pressure;
import com.api.modules.temperature.Temperature;
import com.api.modules.tension.Tension;
import com.api.modules.user.User;
import com.api.modules.wind.Wind;
import jakarta.persistence.*;
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
