package br.com.fiap.mottu_api;

import br.com.fiap.mottu_api.repository.MotoRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "MotoVision API", description = "API de gestão de motos e pátios - Challenge FIAP 2025", version = "1.0"))
public class MottuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MottuApiApplication.class, args);
	}


}