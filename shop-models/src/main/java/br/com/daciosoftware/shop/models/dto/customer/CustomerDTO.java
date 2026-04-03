package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.models.validator.CPF;
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
public class CustomerDTO {

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
	private String keyAuth;

	public static CustomerDTO convert(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setNome(customer.getNome());
		customerDTO.setCpf(customer.getCpf());
		customerDTO.setEndereco(customer.getEndereco());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setTelefone(customer.getTelefone());
		customerDTO.setKeyAuth(customer.getKeyAuth());
		customerDTO.setDataCadastro(customer.getDataCadastro());
		if (customer.getInteresses() != null)
			customerDTO.setInteresses(customer.getInteresses().stream().map(CategoryDTO::convert).collect(Collectors.toSet()));
		return customerDTO;
	}

}
