package br.com.bruno.controllers.security;

import br.com.bruno.data.dto.v1.security.AccountCredentialsDto;
import br.com.bruno.services.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsDto data) {
        if(checkIfParamsIsInvalid(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authService.sigin(data);

        if(Objects.isNull(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return token;
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "refresh/{username}")
    public ResponseEntity refreshToken(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken
    ) {
        if(checkIfParamsIsInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authService.refreshToken(username, refreshToken);

        return Objects.nonNull(token) ?
                ResponseEntity.ok(token) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
    }

    private boolean checkIfParamsIsInvalid(String username, String refreshToken) {
        return Objects.isNull(username) || Objects.isNull(refreshToken) || username.isEmpty() || refreshToken.isEmpty();
    }

    private boolean checkIfParamsIsInvalid(AccountCredentialsDto data) {
        return Objects.isNull(data) || Objects.isNull(data.getUsername()) || Objects.isNull(data.getPassword())
            || data.getUsername().isBlank() || data.getPassword().isBlank();
    }

}
