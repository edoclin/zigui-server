package cn.cug.zigui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cn.cug.zigui.mapper"})
public class ZiguiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZiguiApplication.class, args);
	}

}
