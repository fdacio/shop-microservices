package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateAuthUserDTO {
    private Long id;
    private String nome;
    private String username;
    private String email;
    private Set<RuleDTO> rules;
}
