package com.example.cartAndOrder.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8090/swagger-ui/index.html#/
@Configuration
public class SpringConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart and Order API")
                        .description("API Documentation for Cart and Order Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cart and Order Team")
                                .email("support@cartandorder.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addServersItem(new Server().url("http://localhost:8092")
                        .description("Local Development Server"));
    }
}

