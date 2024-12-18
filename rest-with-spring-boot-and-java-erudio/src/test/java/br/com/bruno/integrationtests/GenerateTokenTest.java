package br.com.bruno.integrationtests;

import br.com.bruno.data.dto.v1.security.TokenDto;
import br.com.bruno.integrationtests.service.AuthorizationService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenerateTokenTest {

    @Autowired
    private AuthorizationService authorizationService;

    private static TokenDto tokenDto;

    @Test
    @Order(0)
    public void testSigin() {

        tokenDto = authorizationService.getTokenDtoFromSignIn("admin", "admin");

        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    public void testRefreshToken() {
        var newTokenDto = authorizationService.getTokenDtoFromRefreshToken(tokenDto);

        assertNotNull(newTokenDto);
        assertNotNull(newTokenDto.getAccessToken());
        assertNotNull(newTokenDto.getRefreshToken());
    }
}
