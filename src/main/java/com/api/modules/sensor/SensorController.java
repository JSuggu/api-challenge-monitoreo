package com.api.modules.sensor;

import com.api.handler.StatusCode;
import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.sensor.dto.SensorCreateDTO;
import com.api.modules.sensor.dto.SensorDefaultCreateDTO;
import com.api.modules.sensor.dto.SensorUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/default-save")
    @Operation(
            summary = "Save 8 sensors",
            description = "Save 8 sensors for the plant selected"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plants created successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "User dont have permission"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Sensor>> saveDefaultSensorsByPlant(@Valid @RequestBody SensorDefaultCreateDTO request) throws CustomNotFoundException, OperationsException {
        List<Sensor> sensors = sensorService.saveDefaultSensorsByPlant(request);
        return ResponseEntity.status(StatusCode.CREATED).body(sensors);
    }

    @Operation(
            summary = "Save sensor",
            description = "Save sensor for the plant selected"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plant created successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "User dont have permissions"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/save")
    public ResponseEntity<Sensor> saveSensor(@Valid @RequestBody SensorCreateDTO request) throws CustomNotFoundException, OperationsException {
        Sensor savedSensor = sensorService.saveSensor(request);
        return ResponseEntity.status(StatusCode.CREATED).body(savedSensor);
    }

    @Operation(
            summary = "Update sensor",
            description = "Update sensor chosen for the plant selected"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plant updated successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "User dont have permission"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Sensor> updateSensor(@Valid @RequestBody SensorUpdateDTO request, @PathVariable(name = "id") Long id) throws CustomNotFoundException {
        Sensor updatedSensor = sensorService.updateSensor(request, id);
        return ResponseEntity.status(StatusCode.CREATED).body(updatedSensor);
    }

    @Operation(
            summary = "Delete sensor",
            description = "Delete sensor selected"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor deleted or sensor dont exist or your dont have permissions"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteSensor(@PathVariable(name = "id") Long id){
        String message = sensorService.deleteSensor(id);
        return ResponseEntity.status(StatusCode.OK).body(message);
    }
}
