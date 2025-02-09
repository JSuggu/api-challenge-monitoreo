package com.api.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User data to log in.")
public class LoginRequest {
    @NotBlank(message = "Username cant be empty")
    @Schema(description = "Username of the user")
    private String username;
    @NotBlank(message = "Password cant be empty")
    @Schema(description = "Password of the user")
    private String password;
}
