package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.auth.service.RuleService;
import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import br.com.daciosoftware.shop.models.dto.auth.RuleEnum;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class RuleConfig implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RuleConfig.class);

    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private RuleService ruleService;
    @Override
    public void run(String... args) {

        RuleEnum[] rules = RuleEnum.values();

        Arrays.stream(rules).forEach(rule -> {
            Optional<Rule> optional = ruleRepository.findByNome(rule.getName());
            if (optional.isEmpty()) {
                RuleDTO newRule = new RuleDTO();
               // newRule.setId(rule.getCode());
                newRule.setNome(rule.getName());
                RuleDTO ruleDTO = ruleService.save(newRule);
                log.info("Rule {} criada com sucesso", ruleDTO.getNome());
            }
        });
    }
}
