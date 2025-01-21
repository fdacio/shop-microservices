package br.com.daciosoftware.shop.product;

import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductNotFoundException;
import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import br.com.daciosoftware.shop.models.entity.product.Category;
import br.com.daciosoftware.shop.models.entity.product.Product;
import br.com.daciosoftware.shop.product.repository.ProductRepository;
import br.com.daciosoftware.shop.product.service.CategoryService;
import br.com.daciosoftware.shop.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Test
	void testFindAll() {
		Mockito.when(productRepository.findAll()).thenReturn(ProductRepositoryMock.getListProducts());
		Assertions.assertEquals(3, productService.findAll().size());
	}

	@Test
	void testFindById() {

		Long id = 1L;

		Optional<Product> product = ProductRepositoryMock.getProductFilterById(id);

		Mockito.when(productRepository.findById(id)).thenReturn(product);

		ProductDTO productDTOResult = productService.findById(id);

		Assertions.assertEquals("Produto 1", productDTOResult.getNome());
	}

	@Test
	void testFindById_Throw_ProductNotFoundException() {

		Long id = 8L;//id not found

		Optional<Product> product = ProductRepositoryMock.getProductFilterById(id);

		Mockito.when(productRepository.findById(id)).thenReturn(product);

		Assertions.assertThrowsExactly(ProductNotFoundException.class, () -> productService.findById(id));
	}

	@Test
	public void testFindByCategory() {

		Long categoryId = 1L;
		List<Product> products = ProductRepositoryMock.getProductsFilterByCategory(categoryId);

		Mockito.when(productRepository.findByCategory(categoryId)).thenReturn(products);

		Assertions.assertEquals(2, productService.findByCategory(categoryId).size());
	}

	@Test
	public void testFindByProductIdentifier() {

		String productIdentifier = "321123";

		Optional<Product> product = ProductRepositoryMock.getProductFilterByIdentifie(productIdentifier);

		Mockito.when(productRepository.findByIdentifier(productIdentifier)).thenReturn(product);

		ProductDTO productDTOResult = productService.findByIdentifier(productIdentifier);

		Assertions.assertEquals("Produto 2", productDTOResult.getNome());
	}

	@Test
	void testFindByIdentifier_Throw_ProductNotFoundException() {

		String productIdentifier = "identifier_not_found";//identificador não existente

		Optional<Product> product = ProductRepositoryMock.getProductFilterByIdentifie(productIdentifier);

		Mockito.when(productRepository.findByIdentifier(productIdentifier)).thenReturn(product);

		Assertions.assertThrowsExactly(ProductNotFoundException.class, () -> productService.findByIdentifier(productIdentifier));
	}

	@Test
	public void testFindByName() {

		String name = "Produto";

		List<Product> products = ProductRepositoryMock.getProductsFilterByName(name);

		Mockito.when(productRepository.findByNomeContainingIgnoreCaseOrderByNome(name)).thenReturn(products);

		List<ProductDTO> productsResult = productService.findByNome(name);

		Assertions.assertEquals(3, productsResult.size());
	}

	@Test
	public void testSave() {

		Product product = new Product();
		product.setNome("Refrigerador");
		product.setDescricao("Descricao do Refrigerador ");
		product.setPreco((float)1000.00);
		product.setIdentifier("123456789");
		Category category = new Category();
		category.setId(1L);
		category.setNome("Eletrodomésticos");
		product.setCategory(category);

		ProductDTO productDTO = ProductDTO.convert(product);

		Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

		ProductDTO resultProduct = productService.save(productDTO);

		Assertions.assertEquals("Refrigerador", resultProduct.getNome());
		Assertions.assertEquals("Eletrodomésticos", resultProduct.getCategory().getNome());
	}

}
