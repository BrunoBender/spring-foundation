package br.com.bruno.security.jwt;


import br.com.bruno.exceptions.InvalidJwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import br.com.bruno.data.dto.v1.security.TokenDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//Serviço responsável por criar o Token
@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validyInMilliseconds = 3600000; //1h

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    public TokenDto createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validyInMilliseconds);
        var accessToken = getAccessToken(username, roles, now, validity);
        var refreshToken = getRefreshToken(username, roles, now);


        return new TokenDto(username, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(
            String username,
            List<String> roles,
            Date now,
            Date validity) {

        //URL do servidor
        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();

        var claims = JwtClaimsSet.builder()
                .issuer(issuerUrl)
                .subject(username)
                .issuedAt(now.toInstant())
                .expiresAt(validity.toInstant())
                .claim("roles", roles)
                .build();

        //Realiza o enconde o token com base na configuração definida no SecurityConfig,
        // com o uso das chaves privadas e públicas
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String getRefreshToken(
            String username,
            List<String> roles,
            Date now) {

        Date validityRefreshToken = new Date(now.getTime() + (validyInMilliseconds * 3)); // Irá valer por 3h

        var claims = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now.toInstant())
                .expiresAt(validityRefreshToken.toInstant())
                .claim("roles", roles)
                .build();

        //Realiza o enconde o token com base na configuração definida no SecurityConfig,
        // com o uso das chaves privadas e públicas
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Authentication getAuthentication(String token) {
        Jwt decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Jwt decodedToken(String token) {
        return jwtDecoder.decode(token);
    }

    //Decodifica o token
    public String resolveToken(HttpServletRequest request) {
     String bearerToken = request.getHeader("Authorization");
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }

        return null;
    }

    //Verifica se o token é válido
    public boolean validateToken(String token) {
        Jwt decodedJWT = decodedToken(token);
        try {
            return !isExpired(decodedJWT);
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }

    private boolean isExpired(Jwt token) {
        return Objects.requireNonNull(token.getExpiresAt()).isBefore(Instant.now());
    }
}
