package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.LoginDTO;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthUserDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }


    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDTO createUser(@RequestBody @Valid CreateAuthUserDTO createAuthUserDTO) {
        return authService.createAutUser(createAuthUserDTO);
    }


    @GetMapping("/user")
    public List<AuthUserDTO> findAll() {
        return authService.findAll();
    }


}
