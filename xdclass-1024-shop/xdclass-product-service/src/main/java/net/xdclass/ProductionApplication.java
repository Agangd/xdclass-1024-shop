package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("net.xdclass.mapper")
@EnableTransactionManagement
public class ProductionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductionApplication.class,args);
    }
}
