package com.example.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.annotation.PostConstruct;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .addSecurityItem(buildSecurity())
                .paths(buildAuthenticationPath())
                .components(buildComponents())
                .info(buildInfo());
    }

    private Paths buildAuthenticationPath(){
        return new Paths()
                .addPathItem("/login", buildAuthenticationPathItem());
    }

    private PathItem buildAuthenticationPathItem(){
        return new PathItem().post(
                new Operation()
                        .addTagsItem("Authentication")
                        .requestBody(buildAuthenticationRequestBody())
                        .responses(buildAuthenticationResponses()));
    }

    private RequestBody buildAuthenticationRequestBody() {
        return new RequestBody().content(
                new Content()
                        .addMediaType("application/json", new MediaType()
                                .schema(new Schema<>().$ref("EmailAndPassword"))));
    }

    private ApiResponses buildAuthenticationResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                        .content(new Content()
                                .addMediaType("application/json",
                                        new MediaType()
                                                .schema(new Schema<>()
                                                        .$ref("Tokens")))))
                .addApiResponse("401", new ApiResponse()
                        .content(new Content().addMediaType("application/json",
                                new MediaType()
                                        .schema(new Schema<>()
                                                .$ref("Unauthorized")))));
    }

    private SecurityRequirement buildSecurity() {
        return new SecurityRequirement().addList(BEARER_AUTH);
    }

    private Info buildInfo() {
        return new Info().title("job-search-service API").version("0.1");
    }

    private Components buildComponents() {

        Schema<?> emailAndPassword = new Schema<>()
                .type("object")
                .description("Email and password")
                .addProperty("email", new Schema<>().type("string"))
                .addProperty("password", new Schema<>().type("string"));

        Schema<?> tokens = new Schema<>()
                .type("object")
                .description("Access and refresh tokens")
                .addProperty("accessToken", new Schema<>().type("string"))
                .addProperty("refreshToken", new Schema<>().type("string"));

        Schema<?> unauthorized = new Schema<>()
                .type("object")
                .description("Unauthorized");

        SecurityScheme securityScheme = new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new Components()
                .addSchemas("EmailAndPassword", emailAndPassword)
                .addSchemas("Tokens", tokens)
                .addSecuritySchemes(BEARER_AUTH, securityScheme)
                .addSchemas("Unauthorized", unauthorized);

    }
    @PostConstruct
    private void configurationSpringDocConfig(){
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(AuthenticationPrincipal.class)
                .addResponseTypeToIgnore(ProcessBuilder.Redirect.class);
    }
}

