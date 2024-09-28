package br.com.daciosoftware.shop.modelos.dto.user;

import br.com.daciosoftware.shop.modelos.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.modelos.entity.user.User;
import br.com.daciosoftware.shop.modelos.validator.CPF;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

	private Long id;
	
	@NotBlank(message="Informe o nome")
	@Size(message="Nome tem que ter no máximo 80 caracteres", max = 80)
	private String nome;
	
	@NotBlank(message="Informe o CPF")
	@Size(message="CPF tem que ter no máximo 11 caracteres", max = 11)
	@CPF
	private String cpf;
	
	@NotBlank(message="Informe o endereço")
	@Size(message="Endereço tem que ter no máximo 100 caracteres", max = 100)
	private String endereco;
	
	@NotBlank(message="Informe o email")
	@Size(message="Email tem que ter no máximo 100 caracteres", max = 100)
	private String email;
	
	@NotBlank(message="Informe o telefone")
	@Size(message="Telefone tem que ter no máximo 20 caracteres", max = 20)
	private String telefone;

	private LocalDateTime dataCadastro;
	private Set<CategoryDTO> interesses;
	@JsonIgnore
	private String key;

	public static UserDTO convert(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setNome(user.getNome());
		userDTO.setCpf(user.getCpf());
		userDTO.setEndereco(user.getEndereco());
		userDTO.setEmail(user.getEmail());
		userDTO.setTelefone(user.getTelefone());
		userDTO.setKey(user.getKey());
		userDTO.setDataCadastro(user.getDataCadastro());
		if (user.getInteresses() != null)
			userDTO.setInteresses(user.getInteresses().stream().map(CategoryDTO::convert).collect(Collectors.toSet()));
		return userDTO;
	}

}
