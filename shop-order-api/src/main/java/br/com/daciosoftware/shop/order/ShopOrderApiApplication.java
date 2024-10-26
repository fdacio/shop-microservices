package br.com.daciosoftware.shop.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "br.com.daciosoftware.shop.order.repository", "br.com.daciosoftware.shop.product.repository", "br.com.daciosoftware.shop.user.repository" })
@ComponentScan(basePackages = { "br.com.daciosoftware.shop.order.*", "br.com.daciosoftware.shop.exceptions.*" })
@EntityScan(basePackages = { "br.com.daciosoftware.shop.models.entity" })
public class ShopOrderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopOrderApiApplication.class, args);
	}

}
