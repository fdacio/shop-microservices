package br.com.daciosoftware.shop.modelos.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="rule")
@Table(name="rule", schema="users")
public class Rule {

    private Long id;
    private String nome;

}
