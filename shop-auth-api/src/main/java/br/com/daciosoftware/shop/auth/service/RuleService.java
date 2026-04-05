package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthRuleNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleDTO findByNome(final String nome) {
        return RuleDTO.convert(ruleRepository.findByNome(nome).orElseThrow(() -> new AuthRuleNotFoundException(nome)));
    }

    public RuleDTO findById(final Long id) {
        return RuleDTO.convert(ruleRepository.findById(id).orElseThrow(() -> new AuthRuleNotFoundException("Rule:" + id)));
    }

    public RuleDTO save(RuleDTO ruleDTO) {
        Rule rule = ruleRepository.save(Rule.convert(ruleDTO));
        return RuleDTO.convert(rule);
    }

}
