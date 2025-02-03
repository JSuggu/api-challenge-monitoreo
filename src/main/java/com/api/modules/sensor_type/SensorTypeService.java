package com.api.modules.sensor_type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorTypeService {
    private final SensorTypeRepository sensorTypeRepository;

    public List<SensorType> getAllSensorsTypes(){
        return sensorTypeRepository.findAll();
    }
}
