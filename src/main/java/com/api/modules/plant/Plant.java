package com.api.modules.plant;

import com.api.modules.carbonmonoxide.CarbonMonoxide;
import com.api.modules.energy.Energy;
import com.api.modules.levels.Levels;
import com.api.modules.othergases.OtherGases;
import com.api.modules.pressure.Pressure;
import com.api.modules.temperature.Temperature;
import com.api.modules.tension.Tension;
import com.api.modules.wind.Wind;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "temperature_id")
    private Temperature temperature;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pressure_id")
    private Pressure pressure;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wind_id")
    private Wind wind;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levels_id")
    private Levels levels;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "energy_id")
    private Energy energy;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tension_id")
    private Tension tension;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carbon_monoxide_id")
    private CarbonMonoxide carbonMonoxide;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "other_gases_id")
    private OtherGases otherGases;

}
