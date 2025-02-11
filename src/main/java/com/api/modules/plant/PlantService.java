package com.api.modules.plant;

import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.user.User;
import com.api.modules.user.UserService;
import com.api.security.auth.AuthService;
import com.api.utils.DTOMapper;
import com.api.utils.ResponseMessageDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserService userService;
    private final AuthService authService;

    public Plant getPlantByUuid(String uuid) throws CustomNotFoundException {
        return plantRepository.findByUuid(uuid).orElseThrow(() -> new CustomNotFoundException("Plant not found"));
    }

    public List<PlantResponseDTO> getAllPlants(){
        return plantRepository.findAll().stream().map(DTOMapper::plantToPlantResponseDTO).toList();
    }

    public List<PlantResponseDTO> getAllPlantsByUser(){
        return plantRepository.findByUser_Uuid(authService.getUserUuid()).stream().map(DTOMapper::plantToPlantResponseDTO).toList();
    }

    @Transactional
    public PlantResponseDTO savePlant (PlantCreateDTO request){
        User dbUser = userService.getUserByUuid(authService.getUserUuid());

        Plant newPlant = Plant
                .builder()
                .name(request.getName())
                .country(request.getCountry())
                .user(dbUser)
                .build();

        Plant savedPlant = plantRepository.save(newPlant);

        return DTOMapper.plantToPlantResponseDTO(savedPlant);
    }

    @Transactional
    public PlantResponseDTO updatePlant (PlantCreateDTO request, String uuid) throws CustomNotFoundException {
        Plant dbPlant = plantRepository.findByUuid(uuid).orElseThrow(() -> new CustomNotFoundException("Plant not found"));

        if(!dbPlant.getUser().getUuid().equals(authService.getUserUuid())) throw new PermissionDeniedDataAccessException("You dont have permission to modify this plant.", null);

        dbPlant.setName(request.getName());
        dbPlant.setCountry(request.getCountry());

        Plant updatedPlant = plantRepository.save(dbPlant);

        return DTOMapper.plantToPlantResponseDTO(updatedPlant);
    }

    @Transactional
    public ResponseMessageDTO deletePlant (String uuid){
        int affectedRows = plantRepository.deleteByUuidAndUserUuid(uuid, authService.getUserUuid());
        String message = affectedRows == 0 ? "The plant you are trying delete doesnt exist or dont have permissons" : "Plant deleted";
        return new ResponseMessageDTO(message);
    }
}
