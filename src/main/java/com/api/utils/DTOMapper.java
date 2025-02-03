package com.api.utils;

import com.api.modules.plant.Plant;
import com.api.modules.plant.PlantResponseDTO;

public abstract class DTOMapper {
    public static PlantResponseDTO plantToPlantResponseDTO(Plant plant){
        return PlantResponseDTO
                .builder()
                .uuid(plant.getUuid())
                .name(plant.getName())
                .country(plant.getCountry())
                .sensors(plant.getSensors())
                .build();
    }
}
