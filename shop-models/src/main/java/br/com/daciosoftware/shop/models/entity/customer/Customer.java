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
	private LocalDateTime dataCadastro;
	@OneToMany
	@JoinTable(schema="customers", name="interests",
				joinColumns = @JoinColumn(name="customer_id"),
				inverseJoinColumns = @JoinColumn(name="category_id"))
	private Set<Category> interesses;

	public static Customer convert(CustomerDTO userDTO) {
		Customer customer = new Customer();
		customer.setId(userDTO.getId());
		customer.setNome(userDTO.getNome());
		customer.setCpf(userDTO.getCpf());
		customer.setEndereco(userDTO.getEndereco());
		customer.setEmail(userDTO.getEmail());
		customer.setTelefone(userDTO.getTelefone());
		customer.setDataCadastro(userDTO.getDataCadastro());
		if (userDTO.getInteresses() != null) 
			customer.setInteresses(userDTO.getInteresses().stream().map(Category::convert).collect(Collectors.toSet()));
		return customer;
	}

}
