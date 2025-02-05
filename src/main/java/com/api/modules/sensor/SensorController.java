package com.api.modules.sensor;

import com.api.handler.Result;
import com.api.handler.custom_exception.CustomNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/default-save")
    public Result saveDefaultSensorsByPlant(@Valid @RequestBody SensorDefaultCreateDTO request) throws CustomNotFoundException, OperationsException {
        List<Sensor> sensors = sensorService.saveDefaultSensorsByPlant(request);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message("Successful sensors added")
                .data(sensors)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Result saveSensor(@Valid @RequestBody SensorCreateDTO request) throws CustomNotFoundException {
        Sensor savedSensor = sensorService.saveSensor(request);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message("Successful sensor created")
                .data(savedSensor)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/update/{id}")
    public Result updateSensor(@Valid @RequestBody SensorCreateDTO request, @PathVariable(name = "id") Long id) throws CustomNotFoundException {
        Sensor savedSensor = sensorService.saveSensor(request);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message("Successful sensor created")
                .data(savedSensor)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{id}")
    public Result deleteSensor(@PathVariable(name = "id") Long id){
        String message = sensorService.deleteSensor(id);
        return Result
                .builder()
                .flag(true)
                .code(201)
                .message(message)
                .build();
    }
}
