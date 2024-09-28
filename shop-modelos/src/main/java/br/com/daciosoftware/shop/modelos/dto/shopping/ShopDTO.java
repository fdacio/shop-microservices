package br.com.daciosoftware.shop.modelos.dto.shopping;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import br.com.daciosoftware.shop.modelos.entity.shopping.Shop;
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
public class ShopDTO {

	private Long id;
	private LocalDateTime data;
	private Float total;
	@NotNull(message="Informe o usuário")
	private UserDTO user;
	@NotNull(message="Informe os itens")
	@NotEmpty(message="Informe pelo menos um item")
	private List<ItemDTO> itens = new ArrayList<>();
	
	public static ShopDTO convert(Shop shop) {
		ShopDTO shopDTO = new ShopDTO();
		shopDTO.setId(shop.getId());
		shopDTO.setData(shop.getData());
		shopDTO.setTotal(shop.getTotal());
		shopDTO.setUser(UserDTO.convert(shop.getUser()));
		List<ItemDTO> itensDTO = shop.getItens()
				.stream()
				.map(ItemDTO::convert)
				.collect(Collectors.toList());
		shopDTO.setItens(itensDTO);
		return shopDTO;
	}

	@Override
	public String toString() {
		return "ShopDTO [id=" + id + ", data=" + data + ", total=" + total + ", user=" + user + ", itens=" + itens
				+ "]";
	}
	
}
