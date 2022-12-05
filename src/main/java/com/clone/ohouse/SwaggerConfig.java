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
                .groupName("store")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clone.ohouse.store"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiShopInfo());
    }
    @Bean
    public Docket apiCommunity(){
        return new Docket(DocumentationType.OAS_30)
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
                .title("swagger for ohouse clone project")
                .description("store API")
                .version("0.20")
                .build();
    }

    private ApiInfo apiCommunityInfo(){
        return new ApiInfoBuilder()
                .title("swagger for ohouse clone project")
                .description("community API <br>" +
                        "커뮤니티 - 사진 API <br>" +
                        "사진, 사진 게시글, card 는 모두 같은 의미이며 이 문서에서 혼용될 수 있습니다.")
                .version("0.10")
                .build();
    }
}
