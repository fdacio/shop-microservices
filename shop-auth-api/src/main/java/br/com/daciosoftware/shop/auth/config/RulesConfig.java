package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.models.dto.auth.RuleEnum;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesConfig implements CommandLineRunner {

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {

        ruleRepository.findByNome(RuleEnum.ADMIN.getName()).ifPresentOrElse(
                (r) -> {
                    System.err.printf("Rule Admin(%d) já cadastrada%n", r.getId());
                },
                () -> {
                    Rule ruleAdmin = new Rule();
                    ruleAdmin.setId(1L);
                    ruleAdmin.setNome("Admin");
                    ruleRepository.save(ruleAdmin);
                });


        ruleRepository.findByNome(RuleEnum.BASIC.getName()).ifPresentOrElse(
                (r) -> {
                    System.err.printf("Rule Basic(%d) já cadastrada%n", r.getId());
                },
                () -> {
                    Rule ruleBasic = new Rule();
                    ruleBasic.setId(2L);
                    ruleBasic.setNome("Basic");
                    ruleRepository.save(ruleBasic);
                });


        ruleRepository.findByNome(RuleEnum.CUSTOMER.getName()).ifPresentOrElse(
                (r) -> {
                    System.err.printf("Rule Customer(%d) já cadastrada%n", r.getId());
                },
                () -> {
                    Rule ruleCustomer = new Rule();
                    ruleCustomer.setId(3L);
                    ruleCustomer.setNome("Customer");
                    ruleRepository.save(ruleCustomer);

                });


    }
}
