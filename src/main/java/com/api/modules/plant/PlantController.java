package com.api.modules.plant;

import com.api.handler.Result;
import com.api.handler.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
public class PlantController {
    private final PlantService plantService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dev")
    public Result getAllPlants(){
        List<PlantResponseDTO> plants = plantService.getAllPlants();
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("Success")
                .data(plants)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin")
    public Result getAllPlantsByUser(){
        List<PlantResponseDTO> plants = plantService.getAllPlantsByUser();
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("Success")
                .data(plants)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/save")
    public Result savePlant(@Valid @RequestBody PlantCreateDTO request){
        PlantResponseDTO savedPlant = plantService.savePlant(request);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message("Successful Save")
                .data(savedPlant)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin/update/{uuid}")
    public Result updatePlant(@Valid @RequestBody PlantCreateDTO request, @PathVariable(name = "uuid") String uuid) throws NotFoundException {
        PlantResponseDTO updatedPlant = plantService.updatePlant(request, uuid);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message("Successful Update")
                .data(updatedPlant)
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/delete/{uuid}")
    public Result deletePlant(@PathVariable(name = "uuid") String uuid){
        String message = plantService.deletePlant(uuid);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.NO_CONTENT)
                .message(message)
                .build();
    }
}
