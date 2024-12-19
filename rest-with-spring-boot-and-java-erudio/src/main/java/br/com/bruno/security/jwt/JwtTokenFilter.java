package br.com.bruno.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public void doFilterInternal(
             @NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain chain
    ) throws IOException, ServletException {
        try {
            tokenProvider.resolveToken(request)
                    .ifPresent(this::authenticateUser);

            chain.doFilter(request, response);
        } catch (JwtValidationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }

    private void authenticateUser(String token) throws JwtValidationException {
        Authentication auth = tokenProvider.getAuthentication(token);

        // Define a autenticação na sessão do Spring
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
