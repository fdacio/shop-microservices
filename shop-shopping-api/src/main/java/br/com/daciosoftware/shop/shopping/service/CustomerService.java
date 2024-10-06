package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.exceptions.exceptions.InvalidUserKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.UserNotFoundException;
import br.com.daciosoftware.shop.models.dto.shopping.ShopDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
	
	@Value("${customer.api.url}")
	private String customerApiURL;
	
	public CustomerDTO findUser(ShopDTO shopDTO) {
		
		String key = shopDTO.getCustomer().getKeyAuth();
		
		try {
			WebClient webClient = WebClient.builder()
					.baseUrl(customerApiURL)
					.build();
			Mono<CustomerDTO> user = webClient
					.get()
					.uri("/customer/"+key+"/key")
					.retrieve()
					.bodyToMono(CustomerDTO.class);
			return user.block();
			
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
	}
	
	
	public CustomerDTO validCustomerKeyAuth(CustomerDTO customerDTO, String customerKeyAuth) {
		try {
			WebClient webClient = WebClient.builder()
					.baseUrl(customerApiURL)
					.build();
			Mono<CustomerDTO> user = webClient
					.post()
					.uri("/customer/valid")
					.bodyValue(customerDTO)
					.header("customerKeyAuth", customerKeyAuth)
					.retrieve()
					.bodyToMono(CustomerDTO.class);
			return user.block();
			
		} catch (Exception e) {
			throw new InvalidUserKeyException();
		}
	}

}
