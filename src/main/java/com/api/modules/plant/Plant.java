package com.api.modules.plant;

import com.api.modules.sensor.Sensor;
import com.api.modules.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    @Builder.Default
    @Column(unique = true, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @OneToMany(mappedBy = "plant", fetch = FetchType.EAGER)
    private List<Sensor> sensors;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;
}
