package com.api.modules.energy;

import com.api.modules.plant.Plant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "energies")
public class Energy {
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
}
