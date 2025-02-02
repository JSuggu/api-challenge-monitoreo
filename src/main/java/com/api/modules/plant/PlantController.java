package com.api.modules.plant;

import com.api.handler.Result;
import com.api.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
public class PlantController {
    private final PlantService plantService;

    @GetMapping({"", "/{user-uuid}"})
    public Result getAllPlant(@PathVariable(name = "user-uuid", required = false) String userUuid){
        List<PlantResponseDTO> plants = plantService.getAllPlants(userUuid);
        return Result
                .builder()
                .flag(true)
                .code(200)
                .message("Success")
                .data(plants)
                .build();
    }

    @PostMapping("/save/{user-uuid}")
    public Result savePlant(@RequestBody PlantCreateDTO request, @PathVariable(name = "user-uuid") String userUuid){
        PlantResponseDTO savedPlant = plantService.savePlant(request, userUuid);
        return Result
                .builder()
                .flag(true)
                .code(200)
                .message("Success Save")
                .data(savedPlant)
                .build();
    }
}
