package com.api.service;

import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.plant.*;
import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.modules.user.UserRepository;
import com.api.security.auth.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlantServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlantRepository plantRepository;
    @Autowired
    PlantService plantService;
    @MockitoBean
    AuthService authService;
    List<User> userList = new ArrayList<>();
    List<Plant> plantList = new ArrayList<>();

    @BeforeAll
    void setUp(){
        User newUser = User.builder().username("username").email("username@gmail.com").password("username1234").role(Role.admin).build();
        User newUser2 = User.builder().username("username2").email("username2@gmail.com").password("username1234").role(Role.admin).build();

        User user = userRepository.save(newUser);
        User user2 = userRepository.save(newUser2);

        Plant newPlant = Plant.builder().name("Plant1").country("Argentina").user(user).build();
        Plant newPlant2 = Plant.builder().name("Plant2").country("Chile").user(user2).build();

        Plant plant = plantRepository.save(newPlant);
        Plant plant2 = plantRepository.save(newPlant2);

        userList.addAll(List.of(user, user2));
        plantList.addAll(List.of(plant, plant2));
    }

    @AfterAll
    void cleanUp(){
        userRepository.deleteAll();
        plantRepository.deleteAll();
    }

    @Order(1)
    @Test
    void getAllPlants_returnPlantsList(){
        List<PlantResponseDTO> plants = plantService.getAllPlants();

        Assertions.assertFalse(plants.isEmpty());
        Assertions.assertEquals(2, plants.size());
        Assertions.assertEquals("Argentina", plants.getFirst().getCountry());
        Assertions.assertEquals("Chile", plants.getLast().getCountry());
    }

    @Order(2)
    @Test
    void getAllPlantsByUser_returnPlantList(){
        User user = userList.getFirst();
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        List<PlantResponseDTO> plants = plantService.getAllPlantsByUser();

        Assertions.assertFalse(plants.isEmpty());
        Assertions.assertEquals(1, plants.size());
        Assertions.assertEquals("Plant1", plants.getFirst().getName());
    }

    @Order(3)
    @Test
    void getAllPlantsByUser_returnEmptyList(){
        when(authService.getUserUuid()).thenReturn(UUID.randomUUID().toString());

        List<PlantResponseDTO> plants = plantService.getAllPlantsByUser();

        Assertions.assertTrue(plants.isEmpty());
    }

    @Test
    void savePlant_validUser_returnPlant(){
        User user = userList.getFirst();
        PlantCreateDTO newPlant = new PlantCreateDTO("NewPlant", "Uruguay");
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        PlantResponseDTO savedPlant = plantService.savePlant(newPlant);

        Assertions.assertEquals("NewPlant", savedPlant.getName());
        Assertions.assertEquals("Uruguay", savedPlant.getCountry());
    }

    @Test
    void savePlant_invalidUser_returnPlant(){
        PlantCreateDTO newPlant = new PlantCreateDTO("NewPlant2", "Uruguay");
        when(authService.getUserUuid()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> plantService.savePlant(newPlant));
    }

    @Test
    void updatePlant_existingPlant_returnUpdatedPlant() throws CustomNotFoundException {
        User user = userList.getFirst();
        Plant plant = plantList.getFirst();
        PlantCreateDTO newPlantData = new PlantCreateDTO("Updated plant", "Brasil");
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        PlantResponseDTO updatedPlant =  plantService.updatePlant(newPlantData,plant.getUuid());

        Assertions.assertEquals(plant.getUuid(), updatedPlant.getUuid());
        Assertions.assertNotEquals(plant.getName(), updatedPlant.getName());
        Assertions.assertNotEquals(plant.getCountry(), updatedPlant.getCountry());
    }

    @Test
    void updatePlant_existingPlantAndWrongUser_throwException() {
        User user = userList.getLast();
        Plant plant = plantList.getFirst();
        PlantCreateDTO newPlantData = new PlantCreateDTO("Updated plant", "Brasil");
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        Assertions.assertThrows(DataAccessException.class, () -> plantService.updatePlant(newPlantData,plant.getUuid()));
    }

    @Test
    void updatePlant_nonExistentPlant_throwException() {
        User user = userList.getFirst();
        PlantCreateDTO newPlantData = new PlantCreateDTO("Updated plant", "Brasil");
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        Assertions.assertThrows(CustomNotFoundException.class, () -> plantService.updatePlant(newPlantData, UUID.randomUUID().toString()));
    }

    @Test
    void deletePlant_existingPlant_okMessage(){
        User user = userList.getLast();
        Plant plant = plantList.getLast();
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        String confirmedMessage = plantService.deletePlant(plant.getUuid());

        Assertions.assertEquals("Plant deleted", confirmedMessage);
    }

    @Test
    void deletePlant_nonExistentPlant_nonPlantMessage(){
        User user = userList.getLast();
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        String nonPlantMessage = plantService.deletePlant(UUID.randomUUID().toString());

        Assertions.assertEquals("The plant you are trying delete doesnt exist or dont have permissons", nonPlantMessage);
    }

    @Test
    void deletePlant_wrongUser_nonUserMessage(){
        User user = userList.getFirst();
        Plant plant = plantList.getLast();
        when(authService.getUserUuid()).thenReturn(user.getUuid());

        String wrongUserMessage = plantService.deletePlant(plant.getUuid());

        Assertions.assertEquals("The plant you are trying delete doesnt exist or dont have permissons", wrongUserMessage);
    }
}
