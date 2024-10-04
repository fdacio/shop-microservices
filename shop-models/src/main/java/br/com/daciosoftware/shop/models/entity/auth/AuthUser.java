package br.com.daciosoftware.shop.models.entity.auth;

import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="authUser")
@Table(name="user", schema="auth")
public class AuthUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	private String nome;
	private String username;
	private String password;
	private String email;
	private String keyToken;
	private String hashRecoveryPassword;
	private LocalDateTime dataCadastro;

	@OneToMany
	@JoinTable(schema="auth", name="user_rule",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="rule_id"))
	private Set<Rule> rules;

	public static AuthUser convert(AuthUserDTO userDTO) {
		AuthUser user = new AuthUser();
		user.setId(userDTO.getId());
		user.setNome(userDTO.getNome());
		user.setUsername(userDTO.getEmail());
		user.setEmail(user.getEmail());
		user.setKeyToken(user.getKeyToken());
		if (userDTO.getRules() != null) {
			user.setRules(userDTO.getRules().stream().map(Rule::convert).collect(Collectors.toSet()));
		}
		return user;
	}

	public static AuthUser convert(CreateAuthUserDTO createAuthUserDTO) {
		AuthUser user = new AuthUser();
		user.setId(createAuthUserDTO.getId());
		user.setNome(createAuthUserDTO.getNome());
		user.setUsername(createAuthUserDTO.getUsername());
		user.setEmail(createAuthUserDTO.getEmail());
		return user;
	}

}
