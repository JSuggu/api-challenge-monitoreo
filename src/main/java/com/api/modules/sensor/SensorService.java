package com.api.modules.sensor;

import com.api.modules.plant.Plant;
import com.api.modules.sensor_type.SensorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;

    public Sensor saveSensor(Plant plant, SensorType sensorType){
        Sensor newSensor = Sensor
                .builder()
                .sensorType(sensorType)
                .plant(plant)
                .build();
        return sensorRepository.save(newSensor);
    }


}
