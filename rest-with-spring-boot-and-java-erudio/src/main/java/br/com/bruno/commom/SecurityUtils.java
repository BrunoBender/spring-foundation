package br.com.bruno.commom;

import br.com.bruno.entities.security.User;
import br.com.bruno.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public User currentUser(JwtAuthenticationToken token) {
        return userRepository
                .findByUserName(token.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
