package com.api.modules.plant;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantCreateDTO {
    @NotBlank(message = "Name cant be empty")
    private String name;
    @NotBlank(message = "Country cant be empty")
    private String country;
}
