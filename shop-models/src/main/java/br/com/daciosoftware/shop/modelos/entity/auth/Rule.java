package br.com.daciosoftware.shop.modelos.entity.auth;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="rule")
@Table(name="rule", schema="users")
public class Rule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String nome;

}
