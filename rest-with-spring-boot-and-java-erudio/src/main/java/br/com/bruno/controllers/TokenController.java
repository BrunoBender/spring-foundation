package br.com.bruno.controllers;

import br.com.bruno.data.dto.LoginRequest;
import br.com.bruno.data.dto.LoginResponse;
import br.com.bruno.entities.Permission;
import br.com.bruno.entities.User;
import br.com.bruno.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(
            JwtEncoder jwtEncoder,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var jwtValue = buildTokenJwtWithParams(user.get(), now, expiresIn);

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

    private String buildTokenJwtWithParams(
            User user,
            Instant issuedAt,
            Long expiresIn
    ) {
        var scopes = user.getPermissions()
                .stream()
                .map(Permission::getDescription)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        //Realiza o enconde o token com base na configuração definida no SecurityConfig,
        // com o uso das chaves privadas e públicas
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
