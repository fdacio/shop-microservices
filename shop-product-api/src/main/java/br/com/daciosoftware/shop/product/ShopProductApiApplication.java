package br.com.daciosoftware.shop.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.daciosoftware.shop.product.repository")
@ComponentScan(basePackages = { "br.com.daciosoftware.shop.product.*", "br.com.daciosoftware.shop.exceptions.*", "br.com.daciosoftware.shop.modelos.*" })
@EntityScan(basePackages = { "br.com.daciosoftware.shop.models.entity" })
public class ShopProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopProductApiApplication.class, args);
	}

}
