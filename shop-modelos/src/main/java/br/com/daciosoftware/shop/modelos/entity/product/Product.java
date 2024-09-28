package br.com.daciosoftware.shop.modelos.entity.product;

import br.com.daciosoftware.shop.modelos.dto.product.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="product")
@Table(name="product", schema = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	private Float preco;
	private String identifier;
	private String foto;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	public static Product convert(ProductDTO productDTO) {		
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setNome(productDTO.getNome());
		product.setDescricao(productDTO.getDescricao());
		product.setPreco(productDTO.getPreco());
		product.setIdentifier(productDTO.getIdentifier());
		if (productDTO.getCategory() != null) {
			product.setCategory(Category.convert(productDTO.getCategory()));
		}
		product.setFoto(productDTO.getFoto());
		return product;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", preco=" + preco
				+ ", productIdentifier=" + identifier + ", category=" + category + "]";
	}
	
}
