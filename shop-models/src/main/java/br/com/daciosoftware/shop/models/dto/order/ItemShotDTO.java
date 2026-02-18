package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import br.com.daciosoftware.shop.models.dto.product.ProductShotDTO;
import br.com.daciosoftware.shop.models.entity.order.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemShotDTO {

	private Long id;
	private Float preco;
	private Integer quantidade;
	private ProductShotDTO product;

	public static ItemShotDTO convert(Item item) {
		ItemShotDTO itemDTO = new ItemShotDTO();
		itemDTO.setId(item.getId());
		itemDTO.setProduct(ProductShotDTO.convert(item.getProduct()));
		itemDTO.setQuantidade(item.getQuantidade());
		itemDTO.setPreco(item.getPreco());
		return itemDTO;
	}
}
