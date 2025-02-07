package com.api.service;

import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantRepository;
import com.api.modules.sensor.Sensor;
import com.api.modules.sensor.SensorRepository;
import com.api.modules.sensor.SensorService;
import com.api.modules.sensor.dto.SensorCreateDTO;
import com.api.modules.sensor.dto.SensorDefaultCreateDTO;
import com.api.modules.sensor.dto.SensorUpdateDTO;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.sensor_type.SensorTypeRepository;
import com.api.modules.sensor_type.SensorTypeService;
import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.modules.user.UserRepository;
import com.api.security.auth.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.management.OperationsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SensorServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlantRepository plantRepository;
    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    SensorTypeRepository sensorTypeRepository;
    @Autowired
    SensorService sensorService;
    @MockitoBean
    SensorTypeService sensorTypeService;
    @MockitoBean
    AuthService authService;
    List<User> userList = new ArrayList<>();
    List<Plant> plantList = new ArrayList<>();
    List<SensorType> sensorTypeList = new ArrayList<>();

    @BeforeAll
    void setUp(){
        User newUser = User.builder().username("username").email("username@gmail.com").password("username1234").role(Role.admin).build();
        User newUser2 = User.builder().username("username2").email("username2@gmail.com").password("username1234").role(Role.admin).build();
        User newUser3 = User.builder().username("username3").email("username3@gmail.com").password("username1234").role(Role.admin).build();
        User user = userRepository.save(newUser);
        User user2 = userRepository.save(newUser2);
        User user3 = userRepository.save(newUser3);

        Plant newPlant = Plant.builder().name("Plant1").country("Argentina").user(user).build();
        Plant newPlant2 = Plant.builder().name("Plant2").country("Chile").user(user2).build();
        Plant newPlant3 = Plant.builder().name("Plant3").country("Brasil").user(user3).build();
        Plant plant = plantRepository.save(newPlant);
        Plant plant2 = plantRepository.save(newPlant2);
        Plant plant3 = plantRepository.save(newPlant3);

        List<String> sensorsType = List.of("type1", "type2", "type3", "type4", "type5", "type6", "type7", "type8");
        List<Sensor> sensors = sensorsType.stream().map(type -> {
            SensorType newType = sensorTypeRepository.save(SensorType.builder().name(type).build());
            sensorTypeList.add(newType);
            Sensor newSensor = Sensor.builder().sensorType(newType).plant(plantRepository.findById(1L).orElseThrow()).build();
            return sensorRepository.save(newSensor);
        }).toList();

        plant.setSensors(sensors);
        plant = plantRepository.save(plant);

        userList.addAll(List.of(user, user2, user3));
        plantList.addAll(List.of(plant, plant2, plant3));
    }

    @AfterAll
    void cleanUp(){
        userRepository.deleteAll();
        plantRepository.deleteAll();
    }

    @Order(1)
    @Test
    void saveDefaultSensors_validPlantAndUser_returnSensors() throws CustomNotFoundException, OperationsException {
        Plant plant2 = plantList.get(1);
        User user2 = userList.get(1);
        SensorDefaultCreateDTO requestData = new SensorDefaultCreateDTO(plant2.getUuid());
        when(authService.getUserUuid()).thenReturn(user2.getUuid());
        when(sensorTypeService.getAllSensorsTypes()).thenReturn(sensorTypeList);


        List<Sensor> sensors = sensorService.saveDefaultSensorsByPlant(requestData);

        Assertions.assertFalse(sensors.isEmpty());
        Assertions.assertEquals(8, sensors.size());
    }

    @Test
    void saveDefaultSensors_validPlantAndUserAndSensorLimitExceeded_throwOperationException() {
        Plant plant = plantList.getFirst();
        User user = userList.getFirst();
        SensorDefaultCreateDTO requestData = new SensorDefaultCreateDTO(plant.getUuid());
        when(authService.getUserUuid()).thenReturn(user.getUuid());
        when(sensorTypeService.getAllSensorsTypes()).thenReturn(sensorTypeList);

        Assertions.assertThrows(OperationsException.class, () -> sensorService.saveDefaultSensorsByPlant(requestData));
    }
    @Order(2)
    @Test
    void saveDefaultSensors_validPlantAndWrongUser_throwPermissionDeniedException() {
        String randomUserUuid = UUID.randomUUID().toString();
        Plant plant3 = plantList.getLast();
        SensorDefaultCreateDTO requestData = new SensorDefaultCreateDTO(plant3.getUuid());
        when(authService.getUserUuid()).thenReturn(randomUserUuid);
        when(sensorTypeService.getAllSensorsTypes()).thenReturn(sensorTypeList);

        Assertions.assertThrows(PermissionDeniedDataAccessException.class, () -> sensorService.saveDefaultSensorsByPlant(requestData));
    }

    @Test
    void saveDefaultSensors_wrongPlantAndValidUser_throwCustomNotFoundException() {
        User user3 = userList.getLast();
        String randomPlantUuid = UUID.randomUUID().toString();
        SensorDefaultCreateDTO requestData = new SensorDefaultCreateDTO(randomPlantUuid);
        when(authService.getUserUuid()).thenReturn(user3.getUuid());
        when(sensorTypeService.getAllSensorsTypes()).thenReturn(sensorTypeList);

        Assertions.assertThrows(CustomNotFoundException.class, () -> sensorService.saveDefaultSensorsByPlant(requestData));
    }

    @Order(3)
    @Test
    void saveSensor_validPlantAndUser_returnSensors() throws CustomNotFoundException, OperationsException {
        Plant plant3 = plantList.getLast();
        User user3 = userList.getLast();
        SensorCreateDTO requestData = new SensorCreateDTO(plant3.getUuid(), "type1");
        when(authService.getUserUuid()).thenReturn(user3.getUuid());
        when(sensorTypeService.getSensorByName("type1")).thenReturn(sensorTypeList.getFirst());

        Sensor sensor = sensorService.saveSensor(requestData);

        Assertions.assertEquals("type1", sensor.getSensorType().getName());
        Assertions.assertEquals(0, sensor.getReading());
        Assertions.assertEquals(0, sensor.getAverageAlerts());
        Assertions.assertEquals(0, sensor.getRedAlerts());
        Assertions.assertTrue(sensor.getEnabled());
        Assertions.assertEquals(plant3.getUuid(), sensor.getPlant().getUuid());
        Assertions.assertEquals(user3.getUuid(), sensor.getPlant().getUser().getUuid());
    }

    @Test
    void saveSensor_validPlantAndUserAndSensorLimitExceeded_throwOperationException() {
        Plant plant = plantList.getFirst();
        User user = userList.getFirst();
        SensorCreateDTO requestData = new SensorCreateDTO(plant.getUuid(), "type1");
        when(authService.getUserUuid()).thenReturn(user.getUuid());
        when(sensorTypeService.getSensorByName("type1")).thenReturn(sensorTypeList.getFirst());

        Assertions.assertThrows(OperationsException.class, () -> sensorService.saveSensor(requestData));
    }

    @Test
    void saveSensor_validPlantAndWrongUser_throwPermissionDeniedException() {
        String randomUserUuid = UUID.randomUUID().toString();
        Plant plant3 = plantList.getLast();
        SensorCreateDTO requestData = new SensorCreateDTO(plant3.getUuid(), "type1");
        when(authService.getUserUuid()).thenReturn(randomUserUuid);
        when(sensorTypeService.getSensorByName("type1")).thenReturn(sensorTypeList.getFirst());

        Assertions.assertThrows(PermissionDeniedDataAccessException.class, () -> sensorService.saveSensor(requestData));
    }

    @Test
    void saveSensor_wrongPlantAndValidUser_throwCustomNotFoundException() {
        User user3 = userList.getLast();
        String randomPlantUuid = UUID.randomUUID().toString();
        SensorCreateDTO requestData = new SensorCreateDTO(randomPlantUuid, "type1");
        when(authService.getUserUuid()).thenReturn(user3.getUuid());
        when(sensorTypeService.getSensorByName("type1")).thenReturn(sensorTypeList.getFirst());

        Assertions.assertThrows(CustomNotFoundException.class, () -> sensorService.saveSensor(requestData));
    }

    @Test
    void updateSensor_validPlantAndUser_returnUpdatedSensor() throws CustomNotFoundException {
        User user = userList.getFirst();
        Plant plant = plantList.getFirst();
        SensorUpdateDTO requestData = new SensorUpdateDTO(plant.getUuid(),10, 5, 1, false);
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        Sensor updatedSensor = sensorService.updateSensor(requestData, 1L);

        Assertions.assertEquals(10, updatedSensor.getReading());
        Assertions.assertEquals(5, updatedSensor.getAverageAlerts());
        Assertions.assertEquals(1, updatedSensor.getRedAlerts());
        Assertions.assertFalse(updatedSensor.getEnabled());
    }

    @Test
    void updateSensor_validPlantAndWrongUser_throwPermissionDeniedException() {
        String randomUserUuid = UUID.randomUUID().toString();
        Plant plant = plantList.getFirst();
        SensorUpdateDTO requestData = new SensorUpdateDTO(plant.getUuid(),5, 2, 1, true);
        when(authService.getUserUuid()).thenReturn(randomUserUuid);

        Assertions.assertThrows(PermissionDeniedDataAccessException.class, () -> sensorService.updateSensor(requestData, 1L));
    }

    @Test
    void updateSensor_wrongPlantAndValidUser_throwCustomNotFoundException() {
        User user = userList.getFirst();
        String randomPlantUuid = UUID.randomUUID().toString();
        SensorUpdateDTO requestData = new SensorUpdateDTO(randomPlantUuid,1, 1, 0, true);
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        Assertions.assertThrows(CustomNotFoundException.class, () -> sensorService.updateSensor(requestData, 1000L));
    }

    @Test
    void deleteSensor_validSensorAndUser_returnOkMessage(){
        User user2 = userList.get(1);
        when(authService.getUserUuid()).thenReturn(user2.getUuid());
        String message = sensorService.deleteSensor(9L);

        Assertions.assertEquals("Sensor deleted", message);
    }

    @Test
    void deleteSensor_validSensorAndWrongUser_returnFailedMessage(){
        String randomUserUuid = UUID.randomUUID().toString();
        when(authService.getUserUuid()).thenReturn(randomUserUuid);

        String message = sensorService.deleteSensor(10L);
        Assertions.assertEquals("Sensor dont exist or your dont have permissions", message);
    }

    @Test
    void deleteSensor_validUserAndWrongSensor_returnFailedMessage(){
        User user = userList.getFirst();
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        String message = sensorService.deleteSensor(1000L);
        Assertions.assertEquals("Sensor dont exist or your dont have permissions", message);
    }

}
