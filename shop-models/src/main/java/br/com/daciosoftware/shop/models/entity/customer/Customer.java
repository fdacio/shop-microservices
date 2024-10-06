package br.com.daciosoftware.shop.models.entity.customer;

import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.product.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="customer")
@Table(name="customer", schema="customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	private String nome;
	private String cpf;
	private String endereco;
	private String email;
	private String telefone;
	private String keyAuth;
	private LocalDateTime dataCadastro;
	@OneToMany
	@JoinTable(schema="customers", name="interests",
				joinColumns = @JoinColumn(name="customer_id"),
				inverseJoinColumns = @JoinColumn(name="category_id"))
	private Set<Category> interesses;

	public static Customer convert(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setId(customerDTO.getId());
		customer.setNome(customerDTO.getNome());
		customer.setCpf(customerDTO.getCpf());
		customer.setEndereco(customerDTO.getEndereco());
		customer.setEmail(customerDTO.getEmail());
		customer.setTelefone(customerDTO.getTelefone());
		customer.setKeyAuth(customer.getKeyAuth());
		customer.setDataCadastro(customerDTO.getDataCadastro());
		if (customerDTO.getInteresses() != null)
			customer.setInteresses(customerDTO.getInteresses().stream().map(Category::convert).collect(Collectors.toSet()));
		return customer;
	}

}
