package com.api.modules.sensor;

import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantService;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeService;
import com.api.security.auth.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import javax.management.OperationsException;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final SensorTypeService sensorTypeService;
    private final PlantService plantService;
    private final EntityManager entityManager;
    private final AuthService authService;

    @Transactional
    public List<Sensor> saveDefaultSensorsByPlant (SensorDefaultCreateDTO request) throws CustomNotFoundException, OperationsException {
        Plant dbPlant = plantService.getPlantByUuid(request.plantUuid);

        if(!dbPlant.getSensors().isEmpty()) throw new OperationsException("You cant add all the sensors");

        if(!dbPlant.getUser().getUuid().equals(authService.getUserUuid())) throw new PermissionDeniedDataAccessException("You dont have permission to modify this plant.", null);

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
    public Sensor saveSensor(SensorCreateDTO request) throws CustomNotFoundException {
        SensorType sensorType = sensorTypeService.getSensorByName(request.getSensorTypeName());
        Plant dbPlant = plantService.getPlantByUuid(request.plantUuid);

        if(!dbPlant.getUser().getUuid().equals(authService.getUserUuid())) throw new PermissionDeniedDataAccessException("You dont have permission to modify this plant.", null);

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
        int rowsAffected = sensorRepository.deleteSensorByIdAndUserUuid(id, authService.getUserUuid());
        return rowsAffected == 0? "Sensor dont exist or your dont have permissions" :  "Sensor deleted";
    }
}
