package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.exceptions.exceptions.InvalidUserKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.UserNotFoundException;
import br.com.daciosoftware.shop.modelos.dto.shopping.ShopDTO;
import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {
	
	@Value("${user.api.url}")
	private String userApiURL;
	
	public UserDTO findUser(ShopDTO shopDTO) {
		
		Long id = shopDTO.getUser().getId();
		
		try {
			WebClient webClient = WebClient.builder()
					.baseUrl(userApiURL)
					.build();
			Mono<UserDTO> user = webClient
					.get()
					.uri("/user/"+id)
					.retrieve()
					.bodyToMono(UserDTO.class);
			return user.block();
			
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
	}
	
	
	public UserDTO validUserKey(UserDTO userDTO, String key) {
		try {
			WebClient webClient = WebClient.builder()
					.baseUrl(userApiURL)
					.build();
			Mono<UserDTO> user = webClient
					.post()
					.uri("/user/valid")
					.bodyValue(userDTO)
					.header("key", key)
					.retrieve()
					.bodyToMono(UserDTO.class);
			return user.block();
			
		} catch (Exception e) {
			throw new InvalidUserKeyException();
		}
	}

}
