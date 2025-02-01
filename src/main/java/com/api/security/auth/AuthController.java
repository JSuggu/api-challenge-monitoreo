package com.api.security.auth;

import com.api.handler.Result;
import com.api.handler.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Result register(@Valid @RequestBody RegisterRequest request){
        String message = authService.register(request);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.CREATED)
                .message(message)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return Result
                .builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("Successful login")
                .data(response)
                .build();
    }
}
