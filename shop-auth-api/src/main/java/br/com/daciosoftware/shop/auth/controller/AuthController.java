package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.models.dto.auth.*;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenDTO refreshToken(@RequestBody TokenDTO token) {
        return authService.refreshToken(token);
    }

    @PostMapping("/user/authenticated")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO userAuthenticated(@RequestHeader("Authorization") String token) {
        return  authService.findAuthenticatedUser(token);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDTO createUser(@RequestBody @Valid CreateAuthUserDTO createAuthUserDTO) {
        return authService.createOperatorUser(createAuthUserDTO);
    }

    @PostMapping("/user/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDTO createUserFromCustomer(@RequestBody @Valid CreateAuthUserDTO createAuthUserDTO) {
        return authService.createUserFromCustomer(createAuthUserDTO);
    }

    @GetMapping("/user")
    public List<AuthUserDTO> findAll() {
        return authService.findAll();
    }

    @GetMapping("/rule")
    public String[] rules() {
        return Arrays.stream(RuleEnum.values()).map(RuleEnum::getName).toArray(String[]::new);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO findById(@PathVariable Long id) {
        return authService.findById(id);
    }

    @GetMapping("/user/{keyToken}/key-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO findByKeyToken(@PathVariable String keyToken) {
        return authService.findByKeyToken(keyToken);
    }

    @GetMapping("/user/search")
    public AuthUserDTO findByUsername(@RequestParam(name = "username") String username) {
        return authService.findByUsername(username);
    }

    @GetMapping("/user/rule/{ruleId}")
    public List<AuthUserDTO> findByRule(@PathVariable Long ruleId) {
        return authService.findByRule(ruleId);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authService.delete(id);
    }

    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO updatePassword(@RequestBody PasswordDTO newPassword, @RequestHeader("Authorization") String token) {
        return authService.updatePassword(newPassword, token);
    }

    @PatchMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserDTO update(@PathVariable Long id, @RequestBody UpdateAuthUserDTO updateAuthUserDTO) {
        return authService.update(id, updateAuthUserDTO);
    }

    @PostMapping("/user/{id}/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomerFromAuthUser(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        return authService.createCustomerFromAuthUser(id, customerDTO);
    }

    @GetMapping("/healthcheck")
    public String healthcheck () {
        return "ok";
    }

}
