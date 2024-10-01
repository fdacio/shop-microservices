package br.com.daciosoftware.shop.auth.repository;

import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule findByNome(String nome);
}
