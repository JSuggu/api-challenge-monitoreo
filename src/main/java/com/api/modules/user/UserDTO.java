package com.api.modules.user;

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
    @NotBlank(message = "Username cant be empty")
    @Size(min = 6, max = 30, message = "Username must be 6 - 30 characters")
    private String username;
    @NotBlank(message = "Email cant be empty")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Username cant be empty")
    private Role role;
}
