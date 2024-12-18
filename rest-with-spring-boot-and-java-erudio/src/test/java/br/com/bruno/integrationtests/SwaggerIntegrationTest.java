package br.com.bruno.integrationtests;

import br.com.bruno.configs.TestConfigs;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.ConnectException;

import static io.restassured.RestAssured.given;

@Slf4j
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class SwaggerIntegrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = TestConfigs.BASE_URI;
        RestAssured.port = TestConfigs.SERVER_PORT;
    }

    @Test
    public void shouldDisplaySwaggerUiPage() {

        var content = "";

        try {
            content = getContent();
        } catch (ConnectException e) {
            log.error("Server not found. Try running the application to validate the integration tests!");
        } finally {
            assertTrue(content.contains("Swagger UI"));
        }
    }

    private String getContent() throws ConnectException {
        return given()
                .when()
                .get("/swagger-ui/index.html")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
    }
}
