package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAuthUserDTO {

	private Long id;
	@NotNull(message="Informe o nome do usuário")
	private String nome;
	@NotNull(message="Informe o nome o username")
	private String username;
	@NotNull(message="Informe o email")
	private String email;
	@NotNull(message="Informe o password")
	private String password;


}
