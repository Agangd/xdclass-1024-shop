package net.xdclass.config;


import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Component
@Data
@EnableOpenApi
public class SwaggerConfiguration {
    @Bean
    public Docket WebApiDoc(){

        return new Docket(DocumentationType.OAS_30)
                .groupName("用户接口文档")
                .pathMapping("/")

                //定义是否开启Swagger,false是关闭，可以通过变量去控制，线上关闭
                .enable(true)

                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.xdclass"))
                //正则请求匹配路劲，并分配到目前项目组
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("1024电商平台")
                .description("微服务接口文档")
                .contact(new Contact("阿刚","https://xdclass.net","1613853412@qq.com"))
                .version("v1.0")
                .build();
    }
}
