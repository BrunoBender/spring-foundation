package br.com.bruno.integrationtests.service;

import br.com.bruno.configs.TestConfigs;
import br.com.bruno.data.dto.v1.security.AccountCredentialsDto;
import br.com.bruno.data.dto.v1.security.TokenDto;
import org.springframework.stereotype.Service;
import static io.restassured.RestAssured.given;

@Service
public class AuthorizationService {

    public TokenDto getTokenDtoFromSignIn(String username, String password) {
        AccountCredentialsDto user = new AccountCredentialsDto(username, password);

        return given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class);
    }

    public TokenDto getTokenDtoFromRefreshToken(TokenDto tokenDto) {
        return given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("username", tokenDto.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class);
    }
}
