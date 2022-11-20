package com.ssafy.guffy;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
//	Swagger-UI 확인 (아래 내용을 바탕으로 변경해서 사용하면 됨)
//	http://localhost:port/{your-app-root}/swagger-ui.html

    // application.properties에 spring.mvc.pathmatch.matching-strategy=ant-path-matcher 해줘야 작동함

        @Bean
        public Docket postsApi() {
           final ApiInfo apiInfo = new ApiInfoBuilder()
                   .title("GUFFY Random Chatting Rest API")
                   .description("<h3>GUFFY에서 제공되는 Rest api의 문서 제공</h3>")
    //               .contact(new Contact("GUFFY", "https://edu.ssafy.com", "guffy@guffy.com"))
                   .license("MIT License")
                   .version("1.0")
                   .build();
           
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .groupName("default")
                    .apiInfo(apiInfo)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.ssafy.guffy.controller"))
                    .build();
            return docket;
        }

        /*
         * private Predicate<String> postPaths() { return regex("/admin/.*"); // admin
         * 밑에 있는 모든 경로만 가져온다 }
         */
}
