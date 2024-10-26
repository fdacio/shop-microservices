package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.order.Order;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	private Long id;
	private LocalDateTime data;
	private Float total;
	private CustomerDTO customer;
	@NotNull(message="Informe os itens")
	@NotEmpty(message="Informe pelo menos um item")
	private List<ItemDTO> itens = new ArrayList<>();
	
	public static OrderDTO convert(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(order.getId());
		orderDTO.setData(order.getData());
		orderDTO.setTotal(order.getTotal());
		orderDTO.setCustomer(CustomerDTO.convert(order.getCustomer()));
		List<ItemDTO> itensDTO = order.getItens()
				.stream()
				.map(ItemDTO::convert)
				.collect(Collectors.toList());
		orderDTO.setItens(itensDTO);
		return orderDTO;
	}

	@Override
	public String toString() {
		return "OrderDTO [id=" + id + ", data=" + data + ", total=" + total + ", user=" + customer + ", itens=" + itens
				+ "]";
	}
	
}
