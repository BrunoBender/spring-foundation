package br.com.bruno.services.security;

import br.com.bruno.data.dto.v1.security.AccountCredentialsDto;
import br.com.bruno.data.dto.v1.security.TokenDto;
import br.com.bruno.entities.security.User;
import br.com.bruno.repositories.security.UserRepository;
import br.com.bruno.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity sigin(AccountCredentialsDto data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();

            var user = findUserByUsername(username);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );


            var tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    public TokenDto refreshToken(String username, String refreshToken) {
        findUserByUsername(username);
        return tokenProvider.refreshToken(refreshToken);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }
}
