package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("net.xdclass.mapper")
public class ProductionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductionApplication.class,args);
    }
}
