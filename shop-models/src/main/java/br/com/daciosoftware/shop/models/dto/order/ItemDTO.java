package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
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
public class ItemDTO {
	
	private Long id;
	private Float preco;

	@NotNull(message="Informe a quantidade")
	@Min(message="Quantidade mínima é 1", value=1)
	private Integer quantidade;
	
	@NotNull(message="Informe o produto")
	private ProductDTO product;
	
	public static ItemDTO convert(Item item) {
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setId(item.getId());
		itemDTO.setProduct(ProductDTO.convert(item.getProduct()));
		itemDTO.setQuantidade(item.getQuantidade());
		itemDTO.setPreco(item.getPreco());
		return itemDTO;
	}

	@Override
	public String toString() {
		return "ItemDTO [product=" + product + ", quantidade=" + quantidade + ", preco=" + preco + "]";
	}
	
}
