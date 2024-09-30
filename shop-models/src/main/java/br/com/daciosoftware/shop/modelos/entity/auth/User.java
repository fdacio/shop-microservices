package br.com.daciosoftware.shop.modelos.entity.auth;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="user")
@Table(name="user", schema="users")
public class User {
	
	@Id
	@Column(name="id")
	private Long id;
	private String nome;
	@Column(name = "email")
	private String username;
	private String password;
	@OneToMany
	@JoinTable(schema="users", name="user_rule",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="rule_id"))
	private Set<Rule> rules;

	public static User convert(UserDTO userDTO) {		
		User user = new User();
		user.setId(userDTO.getId());
		user.setNome(userDTO.getNome());
		user.setUsername(userDTO.getEmail());
		return user;
	}

}
