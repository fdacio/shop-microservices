package br.com.daciosoftware.shop.models.entity.order;

import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "order")
@Table(name = "order", schema = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime data;
	private Float total;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	//@ElementCollection(fetch = FetchType.EAGER)
	//@CollectionTable(name="itens", joinColumns = @JoinColumn(name="shop_id"))
	@OneToMany(
			mappedBy = "shop",
		    orphanRemoval = true,
		    cascade = CascadeType.ALL,
		    fetch = FetchType.EAGER)
	private List<Item> itens = new ArrayList<>();

	public static Order convert(OrderDTO orderDTO) {
		Order order = new Order();
		order.setId(orderDTO.getId());
		order.setData(orderDTO.getData());
		order.setTotal(orderDTO.getTotal());
		order.setCustomer(Customer.convert(orderDTO.getCustomer()));
		List<Item> itens = orderDTO.getItens().stream().map(Item::convert).collect(Collectors.toList());
		itens.forEach((i) -> i.setOrder(order));//Anexar o item ao order
		order.setItens(itens);
		return order;
	}
	
}
