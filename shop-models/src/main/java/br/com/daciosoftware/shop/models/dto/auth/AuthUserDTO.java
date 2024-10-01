package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthUserDTO {

	private Long id;
	private String nome;
	private String username;
	private String password;
	private String email;
	private String token;
	private Set<RuleDTO> rules;
	
	public static AuthUserDTO convert(AuthUser user) {
		AuthUserDTO userDTO = new AuthUserDTO();
		userDTO.setId(user.getId());
		userDTO.setNome(user.getNome());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setPassword("***************");
		if (user.getRules() != null)
			userDTO.setRules(user.getRules().stream().map(RuleDTO::convert).collect(Collectors.toSet()));
		return userDTO;
	}

}
