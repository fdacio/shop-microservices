package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
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
	private String email;
	private Set<RuleDTO> rules;
	private TokenDTO token;
	
	public static AuthUserDTO convert(AuthUser user) {
		AuthUserDTO userDTO = new AuthUserDTO();
		userDTO.setId(user.getId());
		userDTO.setNome(user.getNome());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		if (user.getRules() != null)
			userDTO.setRules(user.getRules().stream().map(RuleDTO::convert).collect(Collectors.toSet()));
		return userDTO;
	}

}
