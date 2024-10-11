package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.auth.service.TokenService;
import br.com.daciosoftware.shop.models.dto.auth.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenConfig;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDTO createUser(@RequestBody @Valid CreateAuthUserDTO createAuthUserDTO) {
        return authService.createUser(createAuthUserDTO);
    }

    @PostMapping("/user/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDTO createUserFromCustomer(@RequestBody @Valid CreateAuthUserDTO createAuthUserDTO) {
        return authService.createUserFromCustomer(createAuthUserDTO);
    }

    @GetMapping("/user")
    public List<AuthUserDTO> findAll() {
        System.err.println("User list all");
        return authService.findAll();
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO findById(@PathVariable Long id) {
        return authService.findById(id);
    }

    @GetMapping("/user/{username}")
    public AuthUserDTO findByUsername(@PathVariable(name = "username") String username) {
        return authService.findByUsername(username);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authService.delete(id);
    }

    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AuthUserDTO updatePassword(@RequestBody PasswordDTO newPassword, @RequestHeader("Authorization") String token) {
        return  authService.updatePassword(newPassword, token);
    }

    @GetMapping("/user/authenticated")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO userAuthenticated(@RequestHeader("Authorization") String token) {
        return  authService.findAuthenticatedUser(token);
    }

    @GetMapping("/jwt-encoder")
    public JwtEncoder getJwtEncoder()  {
        return tokenConfig.jwtEncoder();
    }

    @GetMapping("/healthcheck")
    public String healthcheck () {
        return "ok";
    }

}
