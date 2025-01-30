package com.api.modules.tension;

import com.api.modules.plant.Plant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tensions")
public class Tension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer reading;
    @Column(nullable = false)
    private Integer averageAlerts;
    @Column(nullable = false)
    private Integer redAlerts;
    @Column(nullable = false)
    private Boolean disabled;
    @OneToMany(mappedBy = "tension")
    List<Plant> plants;
}
