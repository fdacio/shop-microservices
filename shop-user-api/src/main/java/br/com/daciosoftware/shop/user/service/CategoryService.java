package br.com.daciosoftware.shop.user.service;

import br.com.daciosoftware.shop.exceptions.exceptions.CategoryNotFoundException;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.models.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

	@Value("${product.api.url}")
	private String productApiURL;

	public Set<CategoryDTO> findCategorysByUser(UserDTO userDTO) {

		Set<CategoryDTO> categorysDTO = new HashSet<>();

		if (userDTO.getInteresses() != null) {

			WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

			for (CategoryDTO c : userDTO.getInteresses()) {
				try {
					Long categoryId = c.getId();
					Mono<CategoryDTO> category = webClient
							.get()
							.uri("/category/" + categoryId)
							.retrieve()
							.bodyToMono(CategoryDTO.class);
					categorysDTO.add(category.block());
				} catch (Exception e) {
					throw new CategoryNotFoundException();
				}
			}
		}

		return categorysDTO;
	}

	public List<CategoryDTO> findAll() {

		WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();
		try {
			Mono<List<CategoryDTO>> response = webClient
					.get()
					.uri("/category")
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<CategoryDTO>>() {});
			return response.block();
		} catch (Exception e) {
			throw new CategoryNotFoundException();
		}
	}
}
