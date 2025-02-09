package com.api.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User data to register.")
public class RegisterRequest {
    @NotBlank(message = "Username cant be empty")
    @Size(min = 6, max = 30, message = "Username must be 6 - 30 characters")
    @Schema(description = "Username of the user")
    private String username;
    @NotBlank(message = "Email cant be empty")
    @Email(message = "Invalid email")
    @Schema(description = "Email of the user")
    private String email;
    @NotBlank(message = "Password cant be empty")
    @Size(min = 8, message = "Password must be greater than 8 characters")
    @Schema(description = "Password of the user")
    private String password;
}
