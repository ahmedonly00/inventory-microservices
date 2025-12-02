package com.pharmacyInventory.inventory.configSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Pharmacy Inventory Management API",
        version = "1.0.0",
        description = "API for managing pharmacy inventory, including medications, brands, categories, and stock",
        contact = @Contact(
            name = "Pharmacy Support",
            email = "support@pharmacyinventory.com",
            url = "https://pharmacyinventory.com/support"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Development"),
        @Server(url = "https://api.pharmacyinventory.com", description = "Production")
    },
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Authentication",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
            .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
            .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new io.swagger.v3.oas.models.security.SecurityScheme()
                        .name(securitySchemeName)
                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("""
                            Enter JWT token in the format: `Bearer <token>`.
                            
                            You can obtain the JWT token by authenticating with your credentials at the `/api/auth/login` endpoint.
                            """)
                )
            )
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Pharmacy Inventory Management API")
                .version("1.0.0")
                .description("""
                    Comprehensive API for managing pharmacy inventory.
                    
                    ## Authentication
                    This API uses JWT for authentication. Include the token in the `Authorization` header.
                    
                    ## Rate Limiting
                    API is rate limited to 1000 requests per hour per IP address.
                    """)
            )
            .externalDocs(new ExternalDocumentation()
                .description("Pharmacy Inventory API Documentation")
                .url("https://docs.pharmacyinventory.com/api")
            );
    }

}