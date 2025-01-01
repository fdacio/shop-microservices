package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.order.OrderNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserKeyTokenDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.entity.order.Order;
import br.com.daciosoftware.shop.order.repository.OrderRepository;
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
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AuthService authService;
	@PersistenceContext
	private EntityManager entityManager;

	private CustomerDTO getCustomerAuthenticated(String token) {
		AuthUserKeyTokenDTO authUserDTO = authService.getUserAuthenticated(token);
		String customerKeyAuth = authUserDTO.getKeyToken();
		return customerService.validCustomerKeyAuth(customerKeyAuth);
	}

	public List<OrderDTO> findAll() {
		
		List<Order> orders = orderRepository.findAll();
		
		return orders
				.stream()
				.map(OrderDTO::convert)
				.collect(Collectors.toList());
	}

	public OrderDTO findById(Long id) {
		return orderRepository.findById(id).map(OrderDTO::convert).orElseThrow(OrderNotFoundException::new);
	}

	public List<OrderDTO> findByCustomerIndentifier(Long userId) {
		List<Order> orders = orderRepository.findByCustomerIdentifier(userId);
		return orders
				.stream()
				.map(OrderDTO::convert)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderDTO save(OrderDTO orderDTO, String token) {

		CustomerDTO customerDTO = getCustomerAuthenticated(token);

		List<ItemDTO> itensDTO = productService.findItens(orderDTO);
		Float total = itensDTO.stream().map(i -> (i.getPreco()*i.getQuantidade()) ).reduce((float)0, Float::sum);
		
		orderDTO.setData(LocalDateTime.now());
		orderDTO.setTotal(total);
		orderDTO.setCustomer(customerDTO);
		orderDTO.setItens(itensDTO);
		
		Order order = Order.convert(orderDTO);
		order = orderRepository.save(order);
		
		return OrderDTO.convert(order);
	}

	public OrderDTO update(Long id, OrderDTO orderDTO, String token) {

		CustomerDTO customerDTO = getCustomerAuthenticated(token);
		if (!orderDTO.getCustomer().getId().equals(customerDTO.getId())) {
			throw new CustomerInvalidKeyException();
		}

		OrderDTO orderUpdateDTO = findById(id);

		List<ItemDTO> itensDTO = productService.findItens(orderDTO);
		Float total = itensDTO.stream().map(i -> (i.getPreco()*i.getQuantidade()) ).reduce((float)0, Float::sum);
		orderUpdateDTO.setTotal(total);
		orderUpdateDTO.setItens(itensDTO);

		Order order = Order.convert(orderUpdateDTO);
		order = orderRepository.save(order);

		return OrderDTO.convert(order);
	}

	public void delete (Long orderId) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			orderRepository.delete(orderOptional.get());
		} else {
			throw new RuntimeException("Venda não encontrada");
		}
	}

	public Page<OrderDTO> findAllPageable(Pageable pageable) {
		return orderRepository.findAll(pageable).map(OrderDTO::convert);
	}

	public List<OrderDTO> findOrdersByFilters(LocalDate dataInicio, LocalDate dataFim, Float valorMinimo) {

		StringBuilder sbSql = new StringBuilder();
		sbSql.append("select o from orders o \n");
		sbSql.append("where o.data >= :dataInicio \n");

		if (dataFim != null) {
			sbSql.append("and o.data <= :dataFim \n");
		}

		if (valorMinimo != null) {
			sbSql.append("and o.total <= :valorMinimo \n");
		}

		sbSql.append("order by o.data");

		TypedQuery<Order> query = entityManager.createQuery(sbSql.toString(), Order.class);

		query.setParameter("dataInicio", dataInicio.atTime(0, 0));

		if (dataFim != null) {
			query.setParameter("dataFim", dataFim.atTime(23, 59));
		}

		if (valorMinimo != null) {
			query.setParameter("valorMinimo", valorMinimo);
		}

		List<Order> vendas = query.getResultList();

		return vendas.stream().map(OrderDTO::convert).toList();

	}

	public List<OrderDTO> findOrdersCustomerAuthenticated(String token) {
		CustomerDTO customerDTO = getCustomerAuthenticated(token);
		return findByCustomerIndentifier(customerDTO.getId());
	}
}
