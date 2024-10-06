package br.com.daciosoftware.shop.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.daciosoftware.shop.customer.repository")
@ComponentScan(basePackages = { "br.com.daciosoftware.shop.customer.*", "br.com.daciosoftware.shop.exceptions.*", "br.com.daciosoftware.shop.modelos.*" })
@EntityScan("br.com.daciosoftware.shop.models.entity")
public class ShopCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopCustomerApplication.class, args);
	}

}
