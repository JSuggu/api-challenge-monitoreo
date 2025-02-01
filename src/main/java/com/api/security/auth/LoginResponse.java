package com.api.security.auth;

import com.api.modules.user.UserDTO;
import com.api.security.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Token token;
    private UserDTO user;
}
