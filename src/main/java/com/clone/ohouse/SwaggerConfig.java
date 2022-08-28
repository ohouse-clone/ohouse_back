package com.clone.ohouse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket apiShop(){
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(true)
                .groupName("shop")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clone.ohouse.shop"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiShopInfo());
    }
    private ApiInfo apiShopInfo(){
        return new ApiInfoBuilder()
                .title("swagger for ohouse clone project")
                .description("store/production API")
                .version("0.1")
                .build();
    }
}
