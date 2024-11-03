package br.com.bruno.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        /*
            -> http://localhost:8080/v3/api-docs
                devolve a documentação do Swagger contendo todas as APIs do projeto, permitindo exportar para po POSTMAN

            -> http://localhost:8080/swagger-ui/index.html
                devolve a documentação das APIs de forma visual em html
        */

        return new OpenAPI()
                .info(
                        new Info()
                            .title("RESTful API with Java 18 and Spring Boot 3")
                            .version("v1")
                            .description("Some description about your API")
                            .termsOfService("")
                            .license(
                                    new License()
                                        .name("Apache 2.0")
                                        .url("")
                            )
                );
    }

}
