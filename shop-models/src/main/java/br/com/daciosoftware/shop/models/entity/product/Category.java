package br.com.daciosoftware.shop.models.entity.product;

import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="category")
@Table(name="category", schema = "products")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	
	public static Category convert(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setNome(categoryDTO.getNome());
		return category;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", nome=" + nome + "]";
	}

}
