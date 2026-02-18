package br.com.daciosoftware.shop.models.dto.product;


import br.com.daciosoftware.shop.models.entity.product.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductShotDTO {

	private Long id;
	private String nome;
	private Float preco;
	private String foto;

	public static ProductShotDTO convert(Product product) {
		ProductShotDTO dto = new ProductShotDTO();
		dto.setId(product.getId());
		dto.setNome(product.getNome());
		dto.setPreco(product.getPreco());
		dto.setFoto(product.getFoto());
		return dto;
	}

}
