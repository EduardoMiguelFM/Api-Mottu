package br.com.fiap.mottu_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Mottu API")
                                                .description("API RESTful para gestão de motos, pátios e usuários da startup Mottu - Projeto FIAP")
                                                .version("1.0.0"));
        }
}
