package br.com.daciosoftware.shop.modelos.entity.user;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import br.com.daciosoftware.shop.modelos.entity.product.Category;
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
@Entity(name="user")
@Table(name="user", schema="users")
public class User {
	
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
	@JoinTable(schema="users", name="interesses",
				joinColumns = @JoinColumn(name="user_id"),
				inverseJoinColumns = @JoinColumn(name="category_id"))
	private Set<Category> interesses;
	private String key;

	//Security
	private String password;

	@JoinTable(schema="users", name="user_rule",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="rule_id"))
	private Set<Rule> rules;

	public static User convert(UserDTO userDTO) {		
		User user = new User();
		user.setId(userDTO.getId());
		user.setNome(userDTO.getNome());
		user.setCpf(userDTO.getCpf());
		user.setEndereco(userDTO.getEndereco());
		user.setEmail(userDTO.getEmail());
		user.setTelefone(userDTO.getTelefone());
		user.setKey(userDTO.getKey());
		user.setDataCadastro(userDTO.getDataCadastro());
		if (userDTO.getInteresses() != null) 
			user.setInteresses(userDTO.getInteresses().stream().map(Category::convert).collect(Collectors.toSet()));
		return user;
	}

}
