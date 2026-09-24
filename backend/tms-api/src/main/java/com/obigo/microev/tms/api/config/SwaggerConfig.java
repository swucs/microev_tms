package com.obigo.microev.tms.api.config;

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
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI openAPI() {
        // 스웨거 설정 정보 생성 및 반환 처리
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .addServersItem(new Server().url("http://localhost:8091" + contextPath).description("Local Server"))
                .addServersItem(new Server().url("http://43.201.173.178" + contextPath).description("Dev Server"))
                ;
    }

    private Info apiInfo() {
        return new Info()
                .title("TMS API")
                .description("TMS - API")
                .version("1.0.0"); // 버전 정보 설정
    }

    private PathItem createLoginPathItem() {
        PathItem pathItem = new PathItem();
        Operation operation = new Operation()
                .addTagsItem("Auth")
                .summary("Login")
                .description("인증을 위한 Login 진행")
                .requestBody(createLoginRequestBody())
                .responses(createLoginApiResponses());

        pathItem.post(operation);
        return pathItem;
    }

    private RequestBody createLoginRequestBody() {
        Map<String, Schema> properties = new LinkedHashMap<>();
        properties.put("loginId", new Schema<>().type("string").example("driver03").description("loginId"));
        properties.put("password", new Schema<>().type("string").example("1234").description("비밀번호").nullable(false));

        Schema<?> schema = new Schema<>().type("object").required(List.of("loginId", "password")).properties(properties);

        Content content = new Content().addMediaType("application/json", new MediaType().schema(schema));
        return new RequestBody().content(content).required(true);
    }

    private ApiResponses createLoginApiResponses() {
        ApiResponses apiResponses = new ApiResponses();
        apiResponses.addApiResponse("200", new ApiResponse().description("Successfully authenticated"));
        apiResponses.addApiResponse("401", new ApiResponse().description("Unauthorized"));
        return apiResponses;
    }
}
