package br.com.daciosoftware.shop.models.entity.order;

import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "orders")
@Table(name = "orders", schema = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime dateOrder;
	private Float total;
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	//@ElementCollection(fetch = FetchType.EAGER)
	//@CollectionTable(name="itens", joinColumns = @JoinColumn(name="shop_id"))
	@OneToMany(
			mappedBy = "order",
		    orphanRemoval = true,
		    cascade = CascadeType.ALL,
		    fetch = FetchType.LAZY)
	private List<Item> itens = new ArrayList<>();

	public static Order convert(OrderDTO orderDTO) {
		Order order = new Order();
		order.setId(orderDTO.getId());
		order.setDateOrder(orderDTO.getDateOrder());
		order.setTotal(orderDTO.getTotal());
		order.setStatus(orderDTO.getStatus());
		order.setCustomer(Customer.convert(orderDTO.getCustomer()));
		List<Item> itens = orderDTO.getItens().stream().map(Item::convert).collect(Collectors.toList());
		itens.forEach((i) -> i.setOrder(order));//Anexar o item ao order
		order.setItens(itens);
		return order;
	}
	
}
