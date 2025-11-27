package com.example.shop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${agigateway.host:http://localhost:8000}")
    private String gatewayHost;

    private static final String SECURITY_SCHEME_NAME ="BearerAuth";


    @Bean
    public OpenAPI openAPI(){

        Server server = new Server();
        server.url(String.format("%s/%s",gatewayHost,appName));
        List<Server> servers = new ArrayList<>();

        servers.add(server);

        SecurityScheme bearerScheme = new SecurityScheme().name("Authorization").type(Type.HTTP).scheme("bearer").bearerFormat("JWT").in(
            In.HEADER);


        return new OpenAPI().info(new Info().title("Shop Service API").version("v1")).servers(servers)
            .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
