package org.training.onlinestoremanagementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for OpenAPI 3.0 integration with Spring Boot applications.
 */
@Configuration
public class OpenAPI30Configuration {

    /**
     * Returns an instance of OpenAPI with a security scheme added to support bearer authentication.
     *
     * @return an instance of OpenAPI configured with the security scheme
     */
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Online Store Management API")
                        .version("1.0.0")
                        .description("The Online Store Management System is a web-based application that allows store owners to manage their online store. The application is built using Spring Boot and uses JWT (JSON Web Token) authentication to secure the RESTful API endpoints.")
                        .contact(new Contact()
                                .name("Karthik Kulkarni")
                                .url("https://github.com/kartik1502/Online-Store-Management-System")
                                .email("kartikkulkarni1411@gmail.com")));
    }

}
