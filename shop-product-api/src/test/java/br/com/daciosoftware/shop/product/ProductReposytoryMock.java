package br.com.daciosoftware.shop.product;

import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.modelos.dto.product.ProductDTO;
import br.com.daciosoftware.shop.modelos.entity.product.Category;
import br.com.daciosoftware.shop.modelos.entity.product.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductReposytoryMock {

	// Construtores
	public class ProductMock {

		public static class PruductBuilder {
			
			private Long id;
			private String nome;
			private String descricao;
			private Float preco;
			private String identifier;
			private Category category;

			public PruductBuilder setId(Long id) {
				this.id = id;
				return this;
			}

			public PruductBuilder setNome(String nome) {
				this.nome = nome;
				return this;
			}

			public PruductBuilder setDescricao(String descricao) {
				this.descricao = descricao;
				return this;
			}

			public PruductBuilder setPreco(Float preco) {
				this.preco = preco;
				return this;
			}

			public PruductBuilder setIdentifier(String identifier) {
				this.identifier = identifier;
				return this;
			}

			public PruductBuilder setCategory(Category category) {
				this.category = category;
				return this;
			}

			public Product build() {
				Product product = new Product();
				product.setId(id);
				product.setNome(nome);
				product.setDescricao(descricao);
				product.setPreco(preco);
				product.setIdentifier(identifier);
				product.setCategory(category);
				return product;
			}
		}
	}
	

	public static Category getCategory(Long id, String nome) {
		Category category = new Category();
		category.setId(id);
		category.setNome(nome);
		return category;
	}

	// Para testes dos Services
	public static List<Product> getListProducts() {

		Product product1 = new ProductMock.PruductBuilder()
				.setId(1L)
				.setNome("Produto 1")
				.setDescricao("Descrição produto1")
				.setPreco((float) 1.00)
				.setIdentifier("123456")
				.setCategory(getCategory(1L, "Category 1"))
				.build();

		Product product2 = new ProductMock.PruductBuilder()
				.setId(2L)
				.setNome("Produto 2")
				.setDescricao("Descrição produto2")
				.setPreco((float) 2.00)
				.setIdentifier("321123")
				.setCategory(getCategory(2L, "Category 2"))
				.build();

		Product product3 = new ProductMock.PruductBuilder()
				.setId(3L)
				.setNome("Produto 3")
				.setDescricao("Descrição produto3")
				.setPreco((float) 5.00)
				.setIdentifier("312346")
				.setCategory(getCategory(1L, "Category 1"))
				.build();

        return Arrays.asList(product1, product2, product3);
	}

	public static Optional<Product> getProductFilterById(Long id) {

		return getListProducts().stream().filter(p -> p.getId() == id).findFirst();
	}

	public static Optional<Product> getProductFilterByIdentifie(String productIdentifie) {

		return getListProducts().stream().filter(p -> p.getIdentifier().equals(productIdentifie)).findFirst();
	}

	public static List<Product> getProductsFilterByCategory(Long categoryIdentifie) {

		return getListProducts().stream().filter(p -> p.getCategory().getId() == categoryIdentifie)
				.collect(Collectors.toList());
	}

	public static List<Product> getProductsFilterByName(String nome) {

		return getListProducts().stream().filter(p -> p.getNome().contains(nome))
				.collect(Collectors.toList());
	}

	// Para testes dos RestController
	public static List<ProductDTO> getListProductsDTO() {

		return getListProducts().stream().map(ProductDTO::convert).collect(Collectors.toList());
	}

	public static ProductDTO getProductDTOFilterById(Long id) {

		return ProductDTO.convert(getProductFilterById(id).orElseThrow(ProductNotFoundException::new));
	}

	public static ProductDTO getProductDTOFilterByIdentifie(String productIdentifie) {
		return ProductDTO.convert(getProductFilterByIdentifie(productIdentifie).get());
	}

	public static List<ProductDTO> getProductsDTOFilterByCategory(Long categoryIdentifie) {
		return getListProducts().stream().map(ProductDTO::convert)
				.filter(p -> p.getCategory().getId() == categoryIdentifie).collect(Collectors.toList());
	}
	
	public static List<ProductDTO> getProductsDTOFilterByName(String nome) {
		return getListProducts().stream().map(ProductDTO::convert).filter(p -> p.getNome().contains(nome))
				.collect(Collectors.toList());
	}
}
