package com.api.modules.sensor;

import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantResponseDTO;
import com.api.modules.plant.PlantService;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeService;
import com.api.utils.DTOMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final SensorTypeService sensorTypeService;
    private final PlantService plantService;
    private final EntityManager entityManager;

    @Transactional
    public List<Sensor> saveDefaultSensorsByPlant (SensorDefaultCreateDTO request){
        Plant dbPlant = plantService.getPlantByUuid(request.plantUuid);

        if(!dbPlant.getSensors().isEmpty()) throw new RuntimeException("You cant add all the sensors");

        if(!dbPlant.getUser().getUuid().equals(request.getUserUuid())) throw new RuntimeException("You dont have permission to edit this plant");

        List<SensorType> sensorTypeList = sensorTypeService.getAllSensorsTypes();

        IntStream.range(0,8).forEach(index -> {
            Sensor newSensor = Sensor
                    .builder()
                    .sensorType(sensorTypeList.get(index))
                    .plant(dbPlant)
                    .build();

            sensorRepository.save(newSensor);
        });

        entityManager.refresh(dbPlant);

        return dbPlant.getSensors();
    }

    @Transactional
    public Sensor saveSensor(SensorCreateDTO request){
        SensorType sensorType = sensorTypeService.getSensorByName(request.getSensorTypeName());
        Plant dbPlant = plantService.getPlantByUuid(request.plantUuid);

        Sensor newSensor = Sensor
                .builder()
                .reading(request.getReading())
                .averageAlerts(request.getAverageAlerts())
                .redAlerts(request.getRedAlerts())
                .disabled(request.getDisabled())
                .sensorType(sensorType)
                .plant(dbPlant)
                .build();

        return sensorRepository.save(newSensor);
    }

    @Transactional
    public String deleteSensor (Long id){
        sensorRepository.deleteById(id);

        return "Sensor deleted";
    }
}
