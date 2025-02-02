package com.api.modules.plant;

import com.api.modules.carbonmonoxide.CarbonMonoxide;
import com.api.modules.energy.Energy;
import com.api.modules.levels.Levels;
import com.api.modules.othergases.OtherGases;
import com.api.modules.pressure.Pressure;
import com.api.modules.temperature.Temperature;
import com.api.modules.user.User;
import com.api.modules.user.UserService;
import com.api.modules.wind.Wind;
import com.api.utils.DTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserService userService;

    public List<PlantResponseDTO> getAllPlants(String userUuid){

        if(Objects.isNull(userUuid)) return plantRepository.findAll().stream().map(DTOMapper::plantToPlantResponseDTO).toList();

        return plantRepository.findByUser_Uuid(userUuid).stream().map(DTOMapper::plantToPlantResponseDTO).toList();
    }

    public PlantResponseDTO savePlant (PlantCreateDTO plant, String uuid){
        User dbUser = userService.getUserByUuid(uuid);

        Plant newPlant = Plant
                .builder()
                .name(plant.getName())
                .country(plant.getCountry())
                .user(dbUser)
                .temperature(new Temperature())
                .pressure(new Pressure())
                .wind(new Wind())
                .levels(new Levels())
                .energy(new Energy())
                .carbonMonoxide(new CarbonMonoxide())
                .otherGases(new OtherGases())
                .build();

        return DTOMapper.plantToPlantResponseDTO(plantRepository.save(newPlant));
    }
}
