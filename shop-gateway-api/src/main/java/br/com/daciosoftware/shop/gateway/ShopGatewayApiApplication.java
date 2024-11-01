package br.com.daciosoftware.shop.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "br.com.daciosoftware.shop.gateway.*", "br.com.daciosoftware.shop.auth.keys.*" })
public class ShopGatewayApiApplication {

	@Value("${auth.api.url}")
	private String authApiUrl;
	@Value("${product.api.url}")
	private String productApiUrl;
	@Value("${customer.api.url}")
	private String customerApiUrl;
	@Value("${order.api.url}")
	private String orderApiUrl;

	public static void main(String[] args) {
		SpringApplication.run(ShopGatewayApiApplication.class, args);
	}
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

		return builder.routes()
				.route("auth_route", r -> r.path("/auth/**").uri(authApiUrl))
				.route("product_route", r -> r.path("/product/**").uri(productApiUrl))
				.route("product_route", r -> r.path("/category/**").uri(productApiUrl))
				.route("customer_route", r -> r.path("/customer/**").uri(customerApiUrl))
				.route("order_route", r -> r.path("/order/**").uri(orderApiUrl))
				.build();
	}

}
