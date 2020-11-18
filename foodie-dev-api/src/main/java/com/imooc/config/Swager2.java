package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class Swager2 {

    /**
     * 配置swagger2核心配置 docket
     * 地址：http://localhost:8088/swagger-ui.html
     * 地址2：http://localhost:8088/doc.html
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        //指定api类型为 swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                //用于定义api文档汇总信息
                .apiInfo(apiInfo())
                //指定controller包
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                //所有controller
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("崽崽天天吃 api文档")
                .contact(new Contact("崽崽", "http://localhost:8088", "1012367424@qq.com"))
                .description("专为崽崽提供的天天吃 api文档")
                //文档版本号
                .version("1.0.0")
                .termsOfServiceUrl("网站地址").build();
    }

}
