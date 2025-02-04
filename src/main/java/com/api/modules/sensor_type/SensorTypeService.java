package com.api.modules.sensor_type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorTypeService {
    private final SensorTypeRepository sensorTypeRepository;

    public SensorType saveSensorType(String name){
        SensorType newSensorType = SensorType.builder().name(name).build();
        return sensorTypeRepository.save(newSensorType);
    }

    public List<SensorType> getAllSensorsTypes(){
        return sensorTypeRepository.findAll();
    }
}
