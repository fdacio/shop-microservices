package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.exceptions.exceptions.ShoppingNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ItemDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ShopDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.shopping.Shop;
import br.com.daciosoftware.shop.shopping.repository.ShoppingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingService {

	@Autowired
	private ShoppingRepository shopRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AuthService authService;
	@PersistenceContext
	private EntityManager entityManager;

	private CustomerDTO getCustomerAuthenticated(String token) {
		AuthUserDTO authUserDTO = authService.getUserAuthenticated(token);
		String customerKeyAuth = authUserDTO.getKeyToken();
		return customerService.validCustomerKeyAuth(customerKeyAuth);
	}

	public List<ShopDTO> findAll() {
		
		List<Shop> shops = shopRepository.findAll();
		
		return shops
				.stream()
				.map(ShopDTO::convert)
				.collect(Collectors.toList());
	}

	public ShopDTO findById(Long id) {
		return shopRepository.findById(id).map(ShopDTO::convert).orElseThrow(ShoppingNotFoundException::new);
	}

	public List<ShopDTO> findByCustomerIndentifier(Long userId) {
		List<Shop> shops = shopRepository.findByCustomerIdentifier(userId);
		return shops
				.stream()
				.map(ShopDTO::convert)
				.collect(Collectors.toList());
	}

	@Transactional
	public ShopDTO save(ShopDTO shopDTO, String token) {

		CustomerDTO customerDTO = getCustomerAuthenticated(token);

		List<ItemDTO> itensDTO = productService.findItens(shopDTO);
		Float total = itensDTO.stream().map(i -> (i.getPreco()*i.getQuantidade()) ).reduce((float)0, Float::sum);
		
		shopDTO.setData(LocalDateTime.now());
		shopDTO.setTotal(total);
		shopDTO.setCustomer(customerDTO);
		shopDTO.setItens(itensDTO);
		
		Shop shop = Shop.convert(shopDTO);
		shop = shopRepository.save(shop);
		
		return ShopDTO.convert(shop);
	}
	
	public void delete (Long shopId) {
		Optional<Shop> shopOptional = shopRepository.findById(shopId);
		if (shopOptional.isPresent()) {
			shopRepository.delete(shopOptional.get());
		} else {
			throw new RuntimeException("Venda não encontrada");
		}
	}

	public Page<ShopDTO> findAllPageable(Pageable pageable) {
		return shopRepository.findAll(pageable).map(ShopDTO::convert);
	}

	public List<ShopDTO> findShopsByFilters(LocalDate dataInicio, LocalDate dataFim, Float valorMinimo) {

		StringBuilder sbSql = new StringBuilder();
		sbSql.append("select s from shop s \n");
		sbSql.append("where s.data >= :dataInicio \n");

		if (dataFim != null) {
			sbSql.append("and s.data <= :dataFim \n");
		}

		if (valorMinimo != null) {
			sbSql.append("and s.total <= :valorMinimo \n");
		}

		sbSql.append("order by s.data");

		TypedQuery<Shop> query = entityManager.createQuery(sbSql.toString(), Shop.class);

		query.setParameter("dataInicio", dataInicio.atTime(0, 0));

		if (dataFim != null) {
			query.setParameter("dataFim", dataFim.atTime(23, 59));
		}

		if (valorMinimo != null) {
			query.setParameter("valorMinimo", valorMinimo);
		}

		List<Shop> vendas = query.getResultList();

		return vendas.stream().map(ShopDTO::convert).toList();

	}

	public List<ShopDTO> findShopsCustomerAuthenticated(String token) {
		CustomerDTO customerDTO = getCustomerAuthenticated(token);
		return findByCustomerIndentifier(customerDTO.getId());
	}
}
