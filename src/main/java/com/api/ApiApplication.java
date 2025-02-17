package com.api;

import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantRepository;
import com.api.modules.sensor.Sensor;
import com.api.modules.sensor.SensorRepository;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeRepository;
import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.modules.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);

	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository, PlantRepository plantRepository,
									SensorTypeRepository sensorTypeRepository, SensorRepository sensorRepository,
									PasswordEncoder passwordEncoder
	) throws Exception{

		return args -> {
			List<String> sensorsType = new ArrayList<>(List.of("temperatura", "presion", "viento", "niveles", "energia", "tension", "monoxido de carbono", "otros gases"));
			sensorsType.forEach(type -> {
				sensorTypeRepository.save(SensorType.builder().name(type).build());
			});

			User user = userRepository.save(User.builder().username("username").email("username@gmail.com").password(passwordEncoder.encode("username1234")).role(Role.admin).build());

			Plant plant1 = plantRepository.save(Plant.builder().name("Quilmes").country("Argentina").user(user).build());
			Plant plant2 = plantRepository.save(Plant.builder().name("Sao Pablo").country("Brasil").user(user).build());
			Plant plant3 = plantRepository.save(Plant.builder().name("La Plata").country("Argentina").user(user).build());
			Plant plant4 = plantRepository.save(Plant.builder().name("Montevideo").country("Uruguay").user(user).build());
			Plant plant5 = plantRepository.save(Plant.builder().name("Asuncion").country("Paraguay").user(user).build());
			Plant plant6 = plantRepository.save(Plant.builder().name("Brasilia").country("Brasil").user(user).build());
			Plant plant7 = plantRepository.save(Plant.builder().name("Valparaiso").country("Chile").user(user).build());

			List<Plant> plants = List.of(plant1, plant2, plant3, plant4, plant5, plant6, plant7);

			List<SensorType> dbTypes = sensorTypeRepository.findAll();


			plants.forEach( plant -> {
				dbTypes.forEach(type -> {
					int readings = (int) (Math.random() * 100)+1;
					int average = (int) (Math.random() * 100)+1;
					int red = (int) (Math.random() * 100)+1;
					sensorRepository.save(Sensor.builder().plant(plant).sensorType(type).readings(readings).averageAlerts(average).redAlerts(red).build());
				});
			});

		};

	}

}
