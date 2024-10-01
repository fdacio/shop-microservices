package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.models.dto.auth.LoginDTO;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public AuthUserDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
