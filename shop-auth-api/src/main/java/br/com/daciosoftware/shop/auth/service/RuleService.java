package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthRuleNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import br.com.daciosoftware.shop.models.dto.auth.RuleEnum;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public RuleDTO findByNome(String name) {

        return RuleDTO.convert(ruleRepository.findByNome(name).orElseThrow(() -> new AuthRuleNotFoundException(RuleEnum.OPERATOR.getName())));
    }

    public RuleDTO save(RuleDTO ruleDTO) {
        Rule rule = ruleRepository.save(Rule.convert(ruleDTO));
        return  RuleDTO.convert(rule);
    }

}
