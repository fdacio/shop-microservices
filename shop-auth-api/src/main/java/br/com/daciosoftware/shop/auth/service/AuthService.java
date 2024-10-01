package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthLoginErrorException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.LoginDTO;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public AuthUserDTO login(LoginDTO loginDTO) {
        AuthUser user = authRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(AuthUserNotFoundException::new);

        boolean loginValid = bCryptPasswordEncoder().matches(loginDTO.getPassword(), user.getPassword());
        if (loginValid) {
            AuthUserDTO authUserDTO = AuthUserDTO.convert(user);
            authUserDTO.setToken("kdjfiadjfpdfiaif9difosidfisapofdips");
            authUserDTO.setExpireToken(1000L);
            return authUserDTO;
        } else {
            throw new AuthLoginErrorException();
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
