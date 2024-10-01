package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ItemDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ShopDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

	@Value("${product.api.url}")
	private String productApiURL;
	
	public List<ItemDTO> findItens(ShopDTO shopDTO) {
		
		List<ItemDTO> itensDTO = new ArrayList<>();
		
		WebClient webClient = WebClient.builder()
				.baseUrl(productApiURL)
				.build();
		
		for (ItemDTO i : shopDTO.getItens()) {
			
			try {
				Long productId = i.getProduct().getId();
				Mono<ProductDTO> product = webClient
						.get()
						.uri("/product/"+productId)
						.retrieve()
						.bodyToMono(ProductDTO.class);
				
				ProductDTO productDTO = product.block();
				i.setProduct(productDTO);
				i.setPreco(productDTO.getPreco());
				itensDTO.add(i);
				
			} catch (Exception e) {
				throw new ProductNotFoundException();
			}
		}

		return itensDTO; 
	}
}
