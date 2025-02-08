package com.api.modules.sensor;

import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantService;
import com.api.modules.sensor.dto.SensorCreateDTO;
import com.api.modules.sensor.dto.SensorDefaultCreateDTO;
import com.api.modules.sensor.dto.SensorUpdateDTO;
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
        Plant dbPlant = plantService.getPlantByUuid(request.getPlantUuid());

        if(!dbPlant.getSensors().isEmpty()) throw new OperationsException("You cant add all the sensors, the limit is 8");

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
    public Sensor saveSensor(SensorCreateDTO request) throws CustomNotFoundException, OperationsException {
        SensorType sensorType = sensorTypeService.getSensorByName(request.getSensorTypeName());
        Plant dbPlant = plantService.getPlantByUuid(request.getPlantUuid());

        if(dbPlant.getSensors().size() >= 8) throw new OperationsException("You cant add more sensors, the limit is 8");

        if(!dbPlant.getUser().getUuid().equals(authService.getUserUuid())) throw new PermissionDeniedDataAccessException("You dont have permission to modify this plant.", null);

        Sensor newSensor = Sensor
                .builder()
                .sensorType(sensorType)
                .plant(dbPlant)
                .build();

        return sensorRepository.save(newSensor);
    }

    @Transactional
    public Sensor updateSensor(SensorUpdateDTO request, Long id) throws CustomNotFoundException {
        Sensor dbSensor = sensorRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("Sensor not found"));

        if(!dbSensor.getPlant().getUser().getUuid().equals(authService.getUserUuid())) throw new PermissionDeniedDataAccessException("You dont have permission to modify this plant.", null);

        dbSensor.setReading(request.getReadings());
        dbSensor.setAverageAlerts(request.getAverageAlerts());
        dbSensor.setRedAlerts(request.getRedAlerts());
        dbSensor.setEnabled(request.getEnabled());

        return sensorRepository.save(dbSensor);
    }

    @Transactional
    public String deleteSensor (Long id){
        int rowsAffected = sensorRepository.deleteSensorByIdAndUserUuid(id, authService.getUserUuid());
        return rowsAffected == 0? "Sensor dont exist or your dont have permissions" :  "Sensor deleted";
    }
}
