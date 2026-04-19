package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.customer.repository.CredcardRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CredcardNotFoundException;
import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CredcardShotDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredcardService {

    @Autowired
    private CredcardRepository credcardRepository;

    public CredcardDTO findById(Long id) {
        return credcardRepository.findById(id).map(CredcardDTO::convert).orElseThrow(CredcardNotFoundException::new);
    }

    public List<CredcardShotDTO> findShotByCustomerId(Long customerId) {
        return credcardRepository.findByCustomerId(customerId).stream()
                .map(CredcardDTO::convert)
                .map(CredcardShotDTO::convert)
                .toList();
    }

    public List<CredcardDTO> findByCustomerId(Long customerId) {
        return credcardRepository.findByCustomerId(customerId).stream().map(CredcardDTO::convert).toList();
    }

    public CredcardDTO save(CredcardDTO credcardDTO) {
        return CredcardDTO.convert(credcardRepository.save(Credcard.convert(credcardDTO)));
    }

    public CredcardDTO updateMyPrincipal(CredcardDTO principalCredcardDTO, CustomerDTO customer) {
        if (principalCredcardDTO.getId() == null || !credcardRepository.existsById(principalCredcardDTO.getId())) {
            throw new CredcardNotFoundException();
        }
        List<CredcardDTO> credcards = credcardRepository.findByCustomerId(customer.getId())
                .stream()
                .map(CredcardDTO::convert)
                .filter(c -> !c.equals(principalCredcardDTO))
                .toList();
        credcards.forEach(c -> {
            c.setPrincipal(false);
            credcardRepository.save(Credcard.convert(c));
        });
        return CredcardDTO.convert(credcardRepository.save(Credcard.convert(principalCredcardDTO)));
    }

    public void deleteById(Long id) {
        credcardRepository.deleteById(id);
    }
}
