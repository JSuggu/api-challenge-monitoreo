package com.api.modules.plant;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantCreateDTO {
    @NotBlank(message = "UserUuid cant be empty")
    private String userUuid;
    @NotBlank(message = "Name cant be empty")
    private String name;
    @NotBlank(message = "Country cant be empty")
    private String country;
}
