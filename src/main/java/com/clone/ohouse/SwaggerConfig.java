package com.clone.ohouse;

import com.clone.ohouse.document.CommunityApiDescription;
import com.clone.ohouse.document.StoreApiDescription;
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
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .groupName("store")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clone.ohouse.store"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiShopInfo());
    }
    @Bean
    public Docket apiCommunity(){
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .groupName("community")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clone.ohouse.community"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiCommunityInfo());
    }
    private ApiInfo apiShopInfo(){
        return new ApiInfoBuilder()
                .title("Store API")
                .description(StoreApiDescription.description)
                .version("1.0")
                .build();
    }

    private ApiInfo apiCommunityInfo(){
        return new ApiInfoBuilder()
                .title("Community API")
                .description(CommunityApiDescription.description)
                .version("1.0")
                .build();
    }
}
