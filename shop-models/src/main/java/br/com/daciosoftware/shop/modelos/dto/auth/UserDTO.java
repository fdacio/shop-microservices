package br.com.daciosoftware.shop.modelos.dto.auth;

import br.com.daciosoftware.shop.modelos.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.modelos.entity.auth.User;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

	private Long id;
	private String nome;
	private String username;
	private String password;
	private Set<RuleDTO> rules;
	
	public static UserDTO convert(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setNome(user.getNome());
		userDTO.setUsername(user.getUsername());
		if (user.getRules() != null)
			userDTO.setRules(user.getRules().stream().map(RuleDTO::convert).collect(Collectors.toSet()));
		return userDTO;
	}

}
