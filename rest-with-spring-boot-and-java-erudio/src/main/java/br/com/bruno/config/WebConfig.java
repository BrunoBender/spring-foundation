package br.com.bruno.config;

import br.com.bruno.serialization.converter.YamlJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        // ------------- MediaType via header ------------
        configurer
                .favorParameter(false) // Aceita parâmetros?
                .ignoreAcceptHeader(false) // ignora parâmetros do header
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON) // Permite JSON
                .mediaType("xml", MediaType.APPLICATION_XML) // Permite XML
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML); // Permite XML

// ------------- MediaType via query params ------------
//        configurer
//                .favorParameter(true) // Aceita parâmetros?
//                .parameterName("mediaType")
//                .ignoreAcceptHeader(true) // ignora parâmetros do header
//                .useRegisteredExtensionsOnly(false)
//                .defaultContentType(MediaType.APPLICATION_JSON)
//                    .mediaType("json", MediaType.APPLICATION_JSON) // Permite JSON
//                    .mediaType("xml", MediaType.APPLICATION_XML); // Permite XML

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(",");


        //CORS definido de forma global, permitindo acesso a todas as APIs da
        //  aplicação e execução de todos os métodos para a lista de origins confiáveis
        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT")
                .allowedMethods("*")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);
    }
}
