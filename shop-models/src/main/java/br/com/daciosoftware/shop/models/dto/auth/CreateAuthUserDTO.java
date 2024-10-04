package br.com.daciosoftware.shop.models.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAuthUserDTO {

	private Long id;
	@NotEmpty(message="Informe o nome do usuário")
	private String nome;
	@NotEmpty(message="Informe o nome o username")
	private String username;
	@NotEmpty(message="Informe o email")
	private String email;
	@NotBlank(message="Informe o password")
	private String password;


}
