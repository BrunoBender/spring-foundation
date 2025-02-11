package br.com.bruno.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file") //Define o prefixo usado no arquivo de configuração
public class FileStorageConfig {

    private String uploadDir;

}
