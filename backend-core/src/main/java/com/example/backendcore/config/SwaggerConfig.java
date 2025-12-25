package com.example.backendcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30) // 使用 OpenAPI 3.0
                .apiInfo(apiInfo())
                .select()
                // 扫描 controller 包下的接口
                .apis(RequestHandlerSelectors.basePackage("com.example.backendcore.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后端接口文档")
                .description("智慧校园能耗监测与管理平台 API 接口文档")
                .contact(new Contact("shiki", "http://localhost:8080", "1479794217@qq.com"))
                .version("1.0")
                .build();
    }
}