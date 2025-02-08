package com.api.controller;

import com.api.handler.StatusCode;
import com.api.modules.plant.*;
import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.security.auth.*;
import com.api.security.jwt.JwtService;
import com.api.security.jwt.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PlantController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlantControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    PlantService plantService;
    @MockitoBean
    AuthService authService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    UserDetailsServiceImpl userDetailsService;
    List<PlantResponseDTO> userPlantList = new ArrayList<>();
    List<PlantResponseDTO> user2PlantList= new ArrayList<>();

    @BeforeAll
    void setUp(){
        User user = User.builder().username("username").email("username@gmail.com").password("username1234").role(Role.admin).build();
        Plant plant = Plant.builder().name("Plant 1").country("Argentina").user(user).build();
        Plant plant2 = Plant.builder().name("Plant 2").country("Argentina").user(user).build();

        User user2 = User.builder().username("username2").email("username2@gmail.com").password("username1234").role(Role.admin).build();
        Plant plant3 = Plant.builder().name("Plant 1").country("Chile").user(user2).build();

        userPlantList.addAll(Stream.of(plant, plant2).map(p -> {
            return PlantResponseDTO.builder().uuid(p.getUuid()).name(p.getName()).country(p.getCountry()).sensors(p.getSensors()).build();
        }).toList());
        user2PlantList.add(PlantResponseDTO.builder().uuid(plant3.getUuid()).name(plant3.getName()).country(plant3.getCountry()).sensors(plant3.getSensors()).build());
    }

    @Test
    void getAllPlants_returnArraySize3() throws Exception {
        List<PlantResponseDTO> allPlants = Stream.concat(userPlantList.stream(), user2PlantList.stream()).toList();
        when(plantService.getAllPlants()).thenReturn(allPlants);

        this.mockMvc.perform(get("/plants/dev"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    void getAllPlantsByUser_returnArraySize2() throws Exception {
        when(plantService.getAllPlants()).thenReturn(userPlantList);

        this.mockMvc.perform(get("/plants/dev"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void savePlant_validPlant_return201() throws Exception {
        PlantCreateDTO requestNewPlant = new PlantCreateDTO("New Plant", "Argentina");
        User user = User.builder().username("newusername").email("newusername@gmail.com").password("username1234").build();
        Plant plant = Plant.builder().name(requestNewPlant.getName()).country(requestNewPlant.getCountry()).user(user).build();
        PlantResponseDTO savedPlant = new PlantResponseDTO(plant.getUuid(), plant.getName(), plant.getCountry(), plant.getSensors());
        when(plantService.savePlant(Mockito.any())).thenReturn(savedPlant);

        this.mockMvc.perform(post("/plants/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewPlant)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
                .andExpect(jsonPath("$.message").value("Successful Save"))
                .andExpect(jsonPath("$.data.name").value("New Plant"))
                .andExpect(jsonPath("$.data.country").value("Argentina"))
                .andExpect(jsonPath("$.data.sensors").doesNotExist());
    }

    @Test
    void savePlant_nullName_throwException() throws Exception {
        PlantCreateDTO requestNewPlant = new PlantCreateDTO("", "Argentina");

        this.mockMvc.perform(post("/plants/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewPlant)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.name").value("Name cant be empty"));
    }

    @Test
    void savePlant_nullCountry_throwException() throws Exception {
        PlantCreateDTO requestNewPlant = new PlantCreateDTO("New Plant", "");

        this.mockMvc.perform(post("/plants/admin/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewPlant)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.country").value("Country cant be empty"));
    }

    @Test
    void updatePlant_validPlant_return201() throws Exception {
        PlantCreateDTO requestUpdatePlant = new PlantCreateDTO("Updated plant", "Uruguay");
        String plantUuid = userPlantList.getFirst().getUuid();
        PlantResponseDTO updatedPlant = userPlantList.getFirst();
        updatedPlant.setName(requestUpdatePlant.getName());
        updatedPlant.setCountry(requestUpdatePlant.getCountry());
        when(plantService.updatePlant(Mockito.any(), Mockito.any())).thenReturn(updatedPlant);

        this.mockMvc.perform(put("/plants/admin/update/"+plantUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUpdatePlant)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
                .andExpect(jsonPath("$.message").value("Successful Update"))
                .andExpect(jsonPath("$.data.uuid").value(userPlantList.getFirst().getUuid()))
                .andExpect(jsonPath("$.data.name").value("Updated plant"))
                .andExpect(jsonPath("$.data.country").value("Uruguay"))
                .andExpect(jsonPath("$.data.sensors").doesNotExist());
    }

    @Test
    void updatePlant_nullName_throwException() throws Exception {
        PlantCreateDTO requestNewPlant = new PlantCreateDTO(null, "Argentina");
        String plantUuid = userPlantList.getFirst().getUuid();

        this.mockMvc.perform(put("/plants/admin/update/"+plantUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewPlant)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.country").value("Name cant be empty"));
    }

    @Test
    void updatePlant_nullCountry_throwException() throws Exception {
        PlantCreateDTO requestNewPlant = new PlantCreateDTO("Update Plant", null);
        String plantUuid = userPlantList.getFirst().getUuid();

        this.mockMvc.perform(put("/plants/admin/update/"+plantUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNewPlant)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.country").value("Country cant be empty"));
    }

    @Test
    void deletePlant_return200() throws Exception {
        String plantUuid = user2PlantList.getFirst().getUuid();
        when(plantService.deletePlant(Mockito.any())).thenReturn("Plant deleted");

        this.mockMvc.perform(delete("/plants/admin/delete/"+plantUuid))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.NO_CONTENT))
                .andExpect(jsonPath("$.message").value("Plant deleted"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
