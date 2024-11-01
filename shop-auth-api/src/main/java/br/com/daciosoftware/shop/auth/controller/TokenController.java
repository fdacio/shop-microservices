package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.component.RsaKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/token")
public class TokenController {

    @Autowired
    private RsaKey rsaKey;

    @GetMapping("/public-key")
    public String publicKey() throws Exception {
        return rsaKey.getPublicKeyDTO().getContent();
    }

}
