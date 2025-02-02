package com.api.security.auth;

import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.modules.user.UserDTO;
import com.api.modules.user.UserRepository;
import com.api.security.jwt.JwtService;
import com.api.security.jwt.Token;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request){
        User newUser = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.admin)
                .build();
        userRepository.save(newUser);

        return "User saved successfully";
    }

    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
          )
        );

        UserDetails user = (User) auth.getPrincipal();
        Token token = new Token(jwtService.generateToken(user));

        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO userDTO = new UserDTO(
                dbUser.getUuid(),
                dbUser.getUsername(),
                dbUser.getEmail(),
                dbUser.getRole()
        );

        return new LoginResponse(token, userDTO);
    }
}
