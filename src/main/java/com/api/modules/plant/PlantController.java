package com.api.modules.plant;

import com.api.handler.StatusCode;
import com.api.utils.ResponseMessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
public class PlantController {
    private final PlantService plantService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dev")
    @Operation(
            summary = "Get all plants for dev",
            description = "Return all plants of the all users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PlantResponseDTO>> getAllPlants(){
        List<PlantResponseDTO> plants = plantService.getAllPlants();
        return ResponseEntity.status(StatusCode.OK).body(plants);
    }

    @Operation(
            summary = "Get all plants for user",
            description = "Returns all the plants belonging to the logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "401", description = "User not authorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin")
    public ResponseEntity<List<PlantResponseDTO>> getAllPlantsByUser(){
        List<PlantResponseDTO> plants = plantService.getAllPlantsByUser();
        return ResponseEntity.status(StatusCode.OK).body(plants);
    }

    @Operation(
            summary = "Save plant",
            description = "Save plant with name and country"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plant created successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/save")
    public ResponseEntity<PlantResponseDTO> savePlant(@Valid @RequestBody PlantCreateDTO request){
        PlantResponseDTO savedPlant = plantService.savePlant(request);
        return ResponseEntity.status(StatusCode.CREATED).body(savedPlant);
    }

    @Operation(
            summary = "Update plant",
            description = "Return the plant updated"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plant updated successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data to update"),
            @ApiResponse(responseCode = "401", description = "User not authorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin/update/{uuid}")
    public ResponseEntity<PlantResponseDTO> updatePlant(@Valid @RequestBody PlantCreateDTO request, @PathVariable(name = "uuid") String uuid) throws NotFoundException {
        PlantResponseDTO updatedPlant = plantService.updatePlant(request, uuid);
        return ResponseEntity.status(StatusCode.CREATED).body(updatedPlant);
    }

    @Operation(
            summary = "Delete plant",
            description = "Return a message confirming the process was successful or not"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plant deleted or process failed because the user does not have permissions."),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/admin/delete/{uuid}")
    public ResponseEntity<ResponseMessageDTO> deletePlant(@PathVariable(name = "uuid") String uuid){
        ResponseMessageDTO response = plantService.deletePlant(uuid);
        return ResponseEntity.status(StatusCode.OK).body(response);
    }
}
