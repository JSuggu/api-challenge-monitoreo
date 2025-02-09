package com.api.security.auth;

import com.api.modules.user.UserDTO;
import com.api.security.jwt.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Schema(description = "Token granted upon login.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik1hcmlhbm8iLCJpYXQiOjE2NzMwNTYwMDAsImV4cCI6MTY3MzA1OTYwMH0." +
            "sZv2kEZk_Ep08yp9ZV_7aapUvoSnFGzZDoTkhABJWiU")
    private Token token;
    @Schema(description = "Data of the user who logged in.", example =
            """
                {
                    "uuid": "9be25516-2117-41fd-8bdc-1185e18c8669",
                    "username": "username",
                    "email": "username@gmai.com",
                    "password": "$2b$10$Sa1qzLhCsQlgwnmxwOBCpey3jC77BdYztykx7EwHQz1AbbPccZW2O"
                }
            """
    )
    private UserDTO user;
}
