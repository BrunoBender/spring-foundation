package br.com.bruno.integrationtests;

import br.com.bruno.configs.TestConfigs;
import br.com.bruno.data.dto.v1.PersonDto;
import br.com.bruno.integrationtests.service.AuthorizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiAccessValidationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDto personDto;

    private static String accessToken;

    @Autowired
    private AuthorizationService authorizationService;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = TestConfigs.BASE_URI;
        RestAssured.port = TestConfigs.SERVER_PORT;

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDto = new PersonDto();
    }

    @Test
    @Order(0)
    public void authorization() {
        accessToken = authorizationService
                .getTokenDtoFromSignIn("admin", "admin")
                .getAccessToken();
    }

    @Test
    public void shouldAllowAccess() throws JsonProcessingException {

        mockPerson();

        specification = getRequestSpecification(TestConfigs.ORIGIN_VALID);

        var content = getContent(201);

        PersonDto requestContent = objectMapper.readValue(content, PersonDto.class);

        assertNotNull(requestContent.getKey());
        assertNotNull(requestContent.getFirstName());
        assertNotNull(requestContent.getLastName());
        assertNotNull(requestContent.getAddress());
        assertNotNull(requestContent.getGender());
        assertTrue(requestContent.getKey() > 0);
    }

    @Test
    public void shouldntAllowAccess() {

        mockPerson();

        specification = getRequestSpecification(TestConfigs.ORIGIN_INVALID);

        var content = getContent(403);

        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        personDto.setFirstName("Richard");
        personDto.setLastName("Stallman");
        personDto.setAddress("New York City, New York, US");
        personDto.setGender("Male");
    }

    private RequestSpecification getRequestSpecification(String origin) {
        return new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, origin)
                .setBasePath("api/person/test")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private String getContent(int statusCode) {
        return given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personDto)
                .when()
                .post()
                .then()
                .statusCode(statusCode)
                .extract()
                .body()
                .asString();
    }
}
