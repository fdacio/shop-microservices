package br.com.daciosoftware.shop.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "br.com.daciosoftware.shop.auth.repository" })
@ComponentScan(basePackages = { "br.com.daciosoftware.shop.auth.*", "br.com.daciosoftware.shop.exceptions.*" })
@EntityScan(basePackages = { "br.com.daciosoftware.shop.models.entity" })
public class ShopAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAuthApiApplication.class, args);
	}

}
