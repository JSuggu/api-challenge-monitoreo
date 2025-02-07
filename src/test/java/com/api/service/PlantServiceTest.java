package com.api.service;

import com.api.modules.plant.PlantRepository;
import com.api.modules.plant.PlantService
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlantServiceTest {
    @Autowired
    PlantRepository plantRepository;
    @Autowired
    com.api.modules.plant.PlantService plantService;
}
