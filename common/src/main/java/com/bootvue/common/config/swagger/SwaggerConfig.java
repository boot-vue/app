package com.bootvue.common.config.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(value = {"app.swagger"}, havingValue = "true")
public class SwaggerConfig {

    @Bean(value = "privateAPI")
    public Docket privateAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bootvue"))
                .paths(PathSelectors.any())
                .build().groupName("privateApi")
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .enableUrlTemplating(false)
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name("token")
                                .description("token信息")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true).defaultValue("")
                                .build()))
                .apiInfo(new ApiInfoBuilder()
                        .title("App接口文档")
                        .version("1.0.0")
                        .build())
                ;
    }

    @Bean(value = "publicApi")
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bootvue.controller.authentication"))
                .paths(PathSelectors.any())
                .build().groupName("publicApi")
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .enableUrlTemplating(false)
                .apiInfo(new ApiInfoBuilder()
                        .title("App接口文档")
                        .version("1.0.0")
                        .build())
                ;
    }

}
