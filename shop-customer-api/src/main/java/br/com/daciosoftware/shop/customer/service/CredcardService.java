package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.customer.repository.CredcardRepository;
import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CredcardShotDTO;
import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredcardService {

    @Autowired
    private CredcardRepository credcardRepository;

    public List<CredcardShotDTO> findShotByCustomerId(Long customerId) {
        return credcardRepository.findByCustomerId(customerId).stream().map(CredcardShotDTO::convert).toList();
    }

    public List<CredcardDTO> findByCustomerId(Long customerId) {
        return credcardRepository.findByCustomerId(customerId).stream().map(CredcardDTO::convert).toList();
    }

    public CredcardDTO save(CredcardDTO credcardDTO) {
        return CredcardDTO.convert(credcardRepository.save(Credcard.convert(credcardDTO)));
    }

    public void deleteById(Long id) {
        credcardRepository.deleteById(id);
    }
}
