package br.com.daciosoftware.shop.customer;

import br.com.daciosoftware.shop.customer.controller.CustomerController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopUserApiApplicationTests {
	
	@Autowired
	private CustomerController userController;
	
	@Test
	void contextLoads() {
		Assertions.assertThat(userController).isNotNull();
	}

}
