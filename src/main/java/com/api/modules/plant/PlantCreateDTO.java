package com.api.modules.plant;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantCreateDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String country;
}
