package com.api.modules.plant;

import com.api.modules.sensors.carbonmonoxide.CarbonMonoxide;
import com.api.modules.sensors.energy.Energy;
import com.api.modules.sensors.levels.Levels;
import com.api.modules.sensors.othergases.OtherGases;
import com.api.modules.sensors.pressure.Pressure;
import com.api.modules.sensors.temperature.Temperature;
import com.api.modules.sensors.tension.Tension;
import com.api.modules.user.User;
import com.api.modules.sensors.wind.Wind;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;
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
