package br.com.daciosoftware.shop.models.entity.auth;

import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "rule")
@Table(name = "rule", schema = "auth")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String nome;

    public static Rule convert(RuleDTO ruleDTO) {
        Rule rule = new Rule();
        rule.setId(ruleDTO.getId());
        rule.setNome(ruleDTO.getNome());
        return rule;
    }

}
