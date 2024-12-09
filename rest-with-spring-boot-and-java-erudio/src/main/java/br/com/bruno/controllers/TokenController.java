package br.com.bruno.controllers;

import br.com.bruno.data.dto.LoginRequest;
import br.com.bruno.data.dto.LoginResponse;
import br.com.bruno.repositories.security.UserRepository;
import br.com.bruno.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var userOp = userRepository.findByUserName(loginRequest.username());

        if (userOp.isEmpty() || !userOp.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        var user = userOp.get();

        var jwtValue = tokenProvider.createAccessToken(user.getUsername(), user.getRoles());

        return ResponseEntity.ok(new LoginResponse(jwtValue.getAccessToken(), jwtValue.getExpiration()));
    }

}
