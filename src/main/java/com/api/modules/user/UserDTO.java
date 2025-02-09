package com.api.modules.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO{
    @Schema(description = "UUID of the user.", example = "9be25516-2117-41fd-8bdc-1185e18c8669")
    private String uuid;
    @Schema(description = "Username of the user.", example = "username")
    private String username;
    @Schema(description = "Email of the user.", example = "username@gmail.com")
    private String email;
    @Schema(description = "Role of the user.", example = "admin")
    private Role role;
}
