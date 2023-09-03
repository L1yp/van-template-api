package org.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.cloud.web.mapper")
@SpringBootApplication
public class VanTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(VanTemplateApplication.class, args);
    }

}
