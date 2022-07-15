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
public class Swagger2 {
    //http://localhost:8088/swagger-ui.html
    //配置swagger2核心配置
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")  //文档页标题
                .contact(new Contact("lh","https//www.baidu.com","1239463490@qq.com")) //联系人信息
                .description("专门写的文档2020.6.28.")      //详细信息
                .version("1.0.1")  //文档版本号
                .termsOfServiceUrl("https//www.baidu.com")
                .build();
    }
}
