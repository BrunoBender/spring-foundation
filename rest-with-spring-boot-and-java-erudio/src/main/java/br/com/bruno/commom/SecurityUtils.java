package br.com.bruno.commom;

import br.com.bruno.entities.User;
import br.com.bruno.repositories.UserRepository;
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
                .findById(Long.valueOf(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
