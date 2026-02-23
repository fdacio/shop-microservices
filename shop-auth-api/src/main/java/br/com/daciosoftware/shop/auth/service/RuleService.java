package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.config.RuleConfig;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthRuleNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private RuleRepository ruleRepository;

    public RuleDTO findByNome(final String nome) {
        return RuleDTO.convert(ruleRepository.findByNome(nome).orElseThrow(() -> new AuthRuleNotFoundException(nome)));
    }

    public RuleDTO findById(final Long id) {
        return RuleDTO.convert(ruleRepository.findById(id).orElseThrow(() -> new AuthRuleNotFoundException("Rule:"+id)));
    }

    public RuleDTO save(RuleDTO ruleDTO) {
        log.info("Perfil {}", ruleDTO);
        Rule rule = ruleRepository.save(Rule.convert(ruleDTO));
        return RuleDTO.convert(rule);
    }

}
