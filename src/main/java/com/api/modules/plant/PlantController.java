package com.api.modules.plant;

import com.api.handler.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
public class PlantController {
    private final PlantService plantService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public Result getAllPlants(){
        List<PlantResponseDTO> plants = plantService.getAllPlants();
        return Result
                .builder()
                .flag(true)
                .code(200)
                .message("Success")
                .data(plants)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user")
    public Result getAllPlantsByUser(){
        List<PlantResponseDTO> plants = plantService.getAllPlantsByUser();
        return Result
                .builder()
                .flag(true)
                .code(200)
                .message("Success")
                .data(plants)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Result savePlant(@Valid @RequestBody PlantCreateDTO request){
        PlantResponseDTO savedPlant = plantService.savePlant(request);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message("Successful Save")
                .data(savedPlant)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update/{uuid}")
    public Result updatePlant(@Valid @RequestBody PlantCreateDTO request, @PathVariable(name = "uuid") String uuid){
        PlantResponseDTO updatedPlant = plantService.updatePlant(request, uuid);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message("Successful Update")
                .data(updatedPlant)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{uuid}")
    public Result deletePlant(@PathVariable(name = "uuid") String uuid){
        String message = plantService.deletePlant(uuid);
        return Result
                .builder()
                .flag(true)
                .code(200)
                .message(message)
                .build();
    }
}
