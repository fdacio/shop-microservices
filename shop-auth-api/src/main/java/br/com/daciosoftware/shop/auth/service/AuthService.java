package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.LoginDTO;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public AuthUserDTO login(LoginDTO loginDTO) {
        AuthUserDTO userDTO = authRepository.findByUsername(loginDTO.getUsername())
                .map(AuthUserDTO::convert)
                .orElseThrow(AuthUserNotFoundException::new);

        userDTO.setToken("kdjfiadjfpdfiaif9difosidfisapofdips");
        return userDTO;
    }


}
