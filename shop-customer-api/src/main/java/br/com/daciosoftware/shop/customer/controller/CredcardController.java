package br.com.daciosoftware.shop.customer.controller;

import br.com.daciosoftware.shop.customer.service.CustomerService;
import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CredcardShotDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CredcardController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/credcard")
    @ResponseStatus(HttpStatus.CREATED)
    public CredcardDTO createCredcard(@RequestBody @Valid CredcardDTO credcard, @RequestHeader("Authorization") String token) {
        return customerService.createCredcard(credcard, token);
    }

    @GetMapping("/my-credcards")
    public List<CredcardShotDTO> getMyCredcards(@RequestHeader("Authorization") String token) {
        return customerService.getMyCredcards(token);
    }

    @PostMapping("/my-principal-credcard")
    public CredcardDTO getMyPrincipalCredcards(@RequestHeader("Authorization") String token) {
        return customerService.getCredcardPrincipal(token);
    }

    @GetMapping("/{id}/my-principal-credcard")
    public CredcardDTO getMyPrincipalCredcardsById(@PathVariable Long id) {
        return customerService.getCredcardPrincipalById(id);
    }

    @PutMapping("/{id}/update-principal-credcard")
    public CredcardDTO updateLikePrincipalCredcard(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return customerService.updatePrincipalCredcard(id, token);
    }

    @DeleteMapping("/{id}/my-credcard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleMyCredcards(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        customerService.deleteMyCredcard(id, token);
    }

}
