package com.api.modules.sensor_type;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorTypeService {
    private final SensorTypeRepository sensorTypeRepository;

    public SensorType getSensorByName(String name){
        return sensorTypeRepository.findByName(name).orElseThrow(() -> new RuntimeException("Sensor type not found"));
    }

    public List<SensorType> getAllSensorsTypes(){
        return sensorTypeRepository.findAll();
    }

    @Transactional
    public SensorType saveSensorType(String name){
        SensorType newSensorType = SensorType.builder().name(name).build();
        return sensorTypeRepository.save(newSensorType);
    }
}
