package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.models.dto.auth.RuleEnum;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RulesConfig implements CommandLineRunner {

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {

        RuleEnum[] rules = RuleEnum.values();

        Arrays.stream(rules).forEach(rule -> {
        ruleRepository.findByNome(rule.getName()).ifPresentOrElse(
                (r) -> {
                    System.err.printf("Rule %s(%d) já cadastrada%n", r.getNome(), r.getId());
                },
                () -> {
                    Rule newRule = new Rule();
                    newRule.setId(rule.getCode());
                    newRule.setNome(rule.getName());
                    ruleRepository.save(newRule);
                });
        });



    }
}
