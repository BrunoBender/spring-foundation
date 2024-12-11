package br.com.bruno.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        tokenProvider.resolveToken((HttpServletRequest) request)
                .ifPresent(this::authenticateUser);

        chain.doFilter(request, response);
    }

    private void authenticateUser(String token) {
        Authentication auth = tokenProvider.getAuthentication(token);

        // Define a autenticação na sessão do Spring
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
