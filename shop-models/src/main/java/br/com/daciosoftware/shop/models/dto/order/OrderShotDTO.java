package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.dto.customer.CustomerShotDTO;
import br.com.daciosoftware.shop.models.entity.order.Order;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
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
public class OrderShotDTO {

	private Long id;
	private LocalDateTime dateOrder;
	private Float total;
	private OrderStatus status;
	private CustomerShotDTO customer;
	private List<ItemShotDTO> itens = new ArrayList<>();
	
	public static OrderShotDTO convert(Order order) {
		OrderShotDTO dto = new OrderShotDTO();
		dto.setId(order.getId());
		dto.setDateOrder(order.getDateOrder());
		dto.setTotal(order.getTotal());
		dto.setStatus(order.getStatus());
		dto.setCustomer(CustomerShotDTO.convert(order.getCustomer()));
		List<ItemShotDTO> itensDTO = order.getItens()
				.stream()
				.map(ItemShotDTO::convert)
				.collect(Collectors.toList());
		dto.setItens(itensDTO);
		return dto;
	}
}
