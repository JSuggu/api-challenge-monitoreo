package com.api.modules.plant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantCreateDTO {
    @NotBlank(message = "Name cant be empty")
    @Schema(description = "Name of the plant.")
    private String name;
    @NotBlank(message = "Country cant be empty")
    @Schema(description = "Country of the plant.")
    private String country;
}
