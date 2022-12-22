package net.xdclass.config;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

//    @Bean
//    LoginInterceptor loginInterceptor(){
//        return new LoginInterceptor();
//    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                //拦截的路劲
                .addPathPatterns("/api/order/*/**")
                //不拦截的路劲
                .excludePathPatterns("/api/callback/*/**","api/order/*/query_state");
    }
}
