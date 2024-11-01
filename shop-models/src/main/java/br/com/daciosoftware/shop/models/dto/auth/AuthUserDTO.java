package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private String nomeSobrenome;
	private String username;
	private String email;
	@JsonIgnore
	private String keyToken;
	private Set<RuleDTO> rules;

	public static AuthUserDTO convert(AuthUser authUser) {
		AuthUserDTO authUserDTO = new AuthUserDTO();
		authUserDTO.setId(authUser.getId());
		authUserDTO.setNome(authUser.getNome());
		authUserDTO.setNomeSobrenome(getNomeSobrenome(authUser.getNome()));
		authUserDTO.setUsername(authUser.getUsername());
		authUserDTO.setEmail(authUser.getEmail());
		authUserDTO.setKeyToken(authUser.getKeyToken());
		if (authUser.getRules() != null)
			authUserDTO.setRules(authUser.getRules().stream().map(RuleDTO::convert).collect(Collectors.toSet()));
		return authUserDTO;
	}

	private static String getNomeSobrenome(String fullName) {
		String[] name = fullName.split(" ");
		return name[0] + " " + name[name.length - 1];
	}
}
