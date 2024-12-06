package br.com.bruno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(Startup.class, args);
	}

}
