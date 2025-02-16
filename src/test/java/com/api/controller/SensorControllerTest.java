package com.api.controller;

import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantResponseDTO;
import com.api.modules.sensor.Sensor;
import com.api.modules.sensor.SensorController;
import com.api.modules.sensor.SensorService;
import com.api.modules.sensor.dto.SensorCreateDTO;
import com.api.modules.sensor.dto.SensorDefaultCreateDTO;
import com.api.modules.sensor.dto.SensorUpdateDTO;
import com.api.modules.sensor_type.SensorType;
import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.security.auth.AuthService;
import com.api.security.jwt.JwtService;
import com.api.security.jwt.UserDetailsServiceImpl;
import com.api.utils.ResponseMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SensorController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SensorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    SensorService sensorService;
    @MockitoBean
    AuthService authService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    UserDetailsServiceImpl userDetailsService;
    List<Sensor> defaultSensors = new ArrayList<>();
    List<PlantResponseDTO> userPlantList = new ArrayList<>();
    List<PlantResponseDTO> user2PlantList= new ArrayList<>();

    @BeforeAll
    void setUp(){
        User user = User.builder().username("username").email("username@gmail.com").password("username1234").role(Role.admin).build();
        Plant plant = Plant.builder().name("Plant 1").country("Argentina").user(user).build();
        Plant plant2 = Plant.builder().name("Plant 2").country("Argentina").user(user).build();

        User user2 = User.builder().username("username2").email("username2@gmail.com").password("username1234").role(Role.admin).build();
        Plant plant3 = Plant.builder().name("Plant 1").country("Chile").user(user2).build();

        List<String> sensorsType = List.of("type1", "type2", "type3", "type4", "type5", "type6", "type7", "type8");
        defaultSensors = sensorsType.stream().map(type -> {
            SensorType newType = SensorType.builder().name(type).build();
            return Sensor.builder().id(Long.valueOf(type.split("")[4])).sensorType(newType).plant(plant).build();
        }).toList();

        plant.setSensors(defaultSensors);

        userPlantList.addAll(Stream.of(plant, plant2).map(p -> {
            return PlantResponseDTO.builder().uuid(p.getUuid()).name(p.getName()).country(p.getCountry()).sensors(p.getSensors()).build();
        }).toList());
        user2PlantList.add(PlantResponseDTO.builder().uuid(plant3.getUuid()).name(plant3.getName()).country(plant3.getCountry()).sensors(plant3.getSensors()).build());
    }

    @Test
    void saveDefaultSensor_returnListSensorsWithSize8() throws Exception {
        SensorDefaultCreateDTO plantUuid = new SensorDefaultCreateDTO(userPlantList.getFirst().getUuid());
        userPlantList.getLast().setSensors(defaultSensors);
        when(sensorService.saveDefaultSensorsByPlant(Mockito.any())).thenReturn(userPlantList.getLast().getSensors());

        this.mockMvc.perform(post("/sensors/admin/default-save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plantUuid)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(8));
    }

    @Test
    void saveDefaultSensor_plantNull_throwException() throws Exception {
        SensorDefaultCreateDTO plantUuid = new SensorDefaultCreateDTO(null);

        this.mockMvc.perform(post("/sensors/admin/default-save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plantUuid)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.plantuuid").value("Plant uuid is mandatory"));
    }

    @Test
    void saveSensor_validSensor_returnSensor() throws Exception {
        String user2PlantUuid = user2PlantList.getFirst().getUuid();
        SensorCreateDTO requestNewSensor = new SensorCreateDTO(user2PlantUuid, "type1");
        Plant plant = Plant.builder().name("Plant x").country("Argentina").build();
        Sensor savedSensor = Sensor.builder().plant(plant).sensorType(SensorType.builder().name("type1").build()).build();

        when(sensorService.saveSensor(Mockito.any())).thenReturn(savedSensor);

        this.mockMvc.perform(post("/sensors/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewSensor)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.reading").value(0))
                .andExpect(jsonPath("$.averageAlerts").value(0))
                .andExpect(jsonPath("$.redAlerts").value(0))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.sensorType.name").value("type1"));
    }

    @Test
    void saveSensor_plantNull_throwException() throws Exception {
        SensorCreateDTO requestNewSensor = new SensorCreateDTO(null, "type1");

        this.mockMvc.perform(post("/sensors/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewSensor)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.plantuuid").value("Plant uuid is mandatory"));
    }

    @Test
    void saveSensor_sensorTypeNameNull_throwException() throws Exception {
        SensorCreateDTO requestNewSensor = new SensorCreateDTO("Plant z", null);

        this.mockMvc.perform(post("/sensors/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewSensor)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.sensortypename").value("Sensor type name cant be empty"));
    }

    @Test
    void updateSensor_validSensor_returnUpdatedSensor() throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().getFirst();
        SensorUpdateDTO requestSensorUpdate = new SensorUpdateDTO(sensor.getPlant().getUuid(),5,3,1, true);
        sensor.setReadings(requestSensorUpdate.getReadings());
        sensor.setAverageAlerts(requestSensorUpdate.getAverageAlerts());
        sensor.setRedAlerts(requestSensorUpdate.getRedAlerts());
        sensor.setEnabled(requestSensorUpdate.getEnabled());

        when(sensorService.updateSensor(Mockito.any(), Mockito.any())).thenReturn(sensor);

        this.mockMvc.perform(put("/sensors/admin/update/"+sensor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSensorUpdate)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.reading").value(5))
                .andExpect(jsonPath("$.averageAlerts").value(3))
                .andExpect(jsonPath("$.redAlerts").value(1))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @ParameterizedTest
    @CsvSource({
            ", Readings cant be null",
            "0, Min value cant be less than 0",
            "10001, Max value cant be greater than 10000"
    })
    void updateSensor_invalidReadings_throwException(Integer readings, String expectedMessage) throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().getFirst();
        SensorUpdateDTO requestSensorUpdate = new SensorUpdateDTO(sensor.getPlant().getUuid(),readings,3,1, true);

        this.mockMvc.perform(put("/sensors/admin/update/"+sensor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSensorUpdate)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.readings").value(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({
            ", Average alerts cant be null",
            "0, Min value cant be less than 0",
            "10001, Max value cant be greater than 10000"
    })
    void updateSensor_invalidAverageAlerts_throwException(Integer averageAlerts, String expectedMessage) throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().getFirst();
        SensorUpdateDTO requestSensorUpdate = new SensorUpdateDTO(sensor.getPlant().getUuid(),5,averageAlerts,1, true);

        this.mockMvc.perform(put("/sensors/admin/update/"+sensor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSensorUpdate)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.averagealerts").value(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({
            ", Red alerts cant be null",
            "0, Min value cant be less than 0",
            "10001, Max value cant be greater than 10000"
    })
    void updateSensor_invalidRedAlerts_throwException(Integer redAlerts, String expectedMessage) throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().getFirst();
        SensorUpdateDTO requestSensorUpdate = new SensorUpdateDTO(sensor.getPlant().getUuid(),5,3,redAlerts, true);

        this.mockMvc.perform(put("/sensors/admin/update/"+sensor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSensorUpdate)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.redalerts").value(expectedMessage));
    }

    @Test
    void deleteSensor_return200() throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().get(5);
        when(sensorService.deleteSensor(sensor.getId())).thenReturn(new ResponseMessageDTO("Sensor deleted"));

        this.mockMvc.perform(delete("/sensors/admin/delete/"+sensor.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message").value("Sensor deleted"));
    }

    @Test
    void deleteSensor_invalidSensorId_return200() throws Exception {
        Sensor sensor = userPlantList.getFirst().getSensors().getFirst();
        when(sensorService.deleteSensor(sensor.getId())).thenReturn(new ResponseMessageDTO("Sensor dont exist or your dont have permissions"));

        this.mockMvc.perform(delete("/sensors/admin/delete/"+sensor.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message").value("Sensor dont exist or your dont have permissions"));
    }
}
