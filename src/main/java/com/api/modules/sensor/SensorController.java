package com.api.modules.sensor;

import com.api.handler.Result;
import com.api.handler.StatusCode;
import com.api.handler.custom_exception.CustomNotFoundException;
import com.api.modules.sensor.dto.SensorCreateDTO;
import com.api.modules.sensor.dto.SensorDefaultCreateDTO;
import com.api.modules.sensor.dto.SensorUpdateDTO;
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
    @PostMapping("/admin/default-save")
    public Result saveDefaultSensorsByPlant(@Valid @RequestBody SensorDefaultCreateDTO request) throws CustomNotFoundException, OperationsException {
        List<Sensor> sensors = sensorService.saveDefaultSensorsByPlant(request);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message("Successful sensors added")
                .data(sensors)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/save")
    public Result saveSensor(@Valid @RequestBody SensorCreateDTO request) throws CustomNotFoundException, OperationsException {
        Sensor savedSensor = sensorService.saveSensor(request);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message("Successful sensor created")
                .data(savedSensor)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/admin/update/{id}")
    public Result updateSensor(@Valid @RequestBody SensorUpdateDTO request, @PathVariable(name = "id") Long id) throws CustomNotFoundException {
        Sensor savedSensor = sensorService.updateSensor(request, id);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message("Successful sensor update")
                .data(savedSensor)
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/delete/{id}")
    public Result deleteSensor(@PathVariable(name = "id") Long id){
        String message = sensorService.deleteSensor(id);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.NO_CONTENT)
                .message(message)
                .build();
    }
}
