package com.api.modules.plant;

import com.api.modules.sensor.Sensor;
import com.api.modules.sensor.SensorService;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeService;
import com.api.modules.user.User;
import com.api.modules.user.UserService;
import com.api.utils.DTOMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserService userService;
    private final SensorService sensorService;
    private final SensorTypeService sensorTypeService;
    private final EntityManager entityManager;

    public List<PlantResponseDTO> getAllPlants(){
        return plantRepository.findAll().stream().map(DTOMapper::plantToPlantResponseDTO).toList();
    }

    public List<PlantResponseDTO> getAllPlantsByUser(String userUuid){
        return plantRepository.findByUser_Uuid(userUuid).stream().map(DTOMapper::plantToPlantResponseDTO).toList();
    }

    @Transactional
    public PlantResponseDTO savePlant (PlantCreateDTO request){
        User dbUser = userService.getUserByUuid(request.getUserUuid());

        Plant newPlant = Plant
                .builder()
                .name(request.getName())
                .country(request.getCountry())
                .user(dbUser)
                .build();

        Plant savedPlant = plantRepository.save(newPlant);

        List<SensorType> sensorTypeList = sensorTypeService.getAllSensorsTypes();

        sensorTypeList.forEach(sensorType -> {
            sensorService.saveSensor(savedPlant, sensorType);
        });

        entityManager.refresh(savedPlant);

        return DTOMapper.plantToPlantResponseDTO(savedPlant);
    }

    @Transactional
    public PlantResponseDTO updatePlant (PlantCreateDTO request, String uuid){
        Plant dbPlant = plantRepository.findByUuid(uuid).orElseThrow();

        dbPlant.setName(request.getName());
        dbPlant.setCountry(request.getCountry());

        Plant updatedPlant = plantRepository.save(dbPlant);

        return DTOMapper.plantToPlantResponseDTO(updatedPlant);
    }

    @Transactional
    public String deletePlant (String uuid){
        int affectedRows = plantRepository.deleteByUuid(uuid);

        return affectedRows == 0 ? "The plant you are trying delete doesnt exist" : "Plant deleted";
    }
}
