package com.api;

import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);

	}

	@Bean
	public CommandLineRunner runner(SensorTypeRepository sensorTypeRepository) throws Exception {
		List<String> types = List.of("temperature", "pressure", "wind", "levels", "energy", "tension", "carbon_monoxide", "other_gases");
		return args -> {
			types.forEach(type -> sensorTypeRepository.save(SensorType.builder().name(type).build()));
		};
	}

}
