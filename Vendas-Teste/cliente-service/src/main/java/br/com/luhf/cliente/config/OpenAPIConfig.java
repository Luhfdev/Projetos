package br.com.luhf.cliente.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .openapi("3.0.0") // Adiciona explicitamente a versão do OpenAPI
            .info(new Info()
                .title("Serviço de Vendas Online")
                .version("1.0.0")
                .description("API para o gerenciamento de vendas, clientes e produtos.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                    .name("Rodrigo Pires")
                    .email("rodrigo@rodrigo.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }
}
