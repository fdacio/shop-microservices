package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.Rule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RuleDTO {


    private Long id;
    private String nome;

    public static RuleDTO convert(Rule rule) {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setId(rule.getId());
        ruleDTO.setNome(rule.getNome());
        return ruleDTO;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RuleDTO other = (RuleDTO) obj;
        return Objects.equals(id, other.id);
    }
}
