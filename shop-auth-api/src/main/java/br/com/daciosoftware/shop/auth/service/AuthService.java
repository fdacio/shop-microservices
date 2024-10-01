package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.config.TokenConfig;
import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthLoginErrorException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.LoginDTO;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.TokenDTO;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RuleRepository ruelRepository;

    @Autowired
    TokenConfig tokenConfig;

    public AuthUserDTO login(LoginDTO loginDTO) {
        AuthUser user = authRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(AuthUserNotFoundException::new);

        boolean loginValid = bCryptPasswordEncoder().matches(loginDTO.getPassword(), user.getPassword());
        if (!loginValid) throw new AuthLoginErrorException();

        //Aqui gerar o token;
        AuthUserDTO authUserDTO = AuthUserDTO.convert(user);
        authUserDTO.setToken(tokenConfig.getToken(authUserDTO));
        return authUserDTO;

    }

    public AuthUserDTO createAutUser(CreateAuthUserDTO createAuthUserDTO) {
        Rule rule = ruelRepository.findByNome("Basic");
        AuthUser authUser = AuthUser.convert(createAuthUserDTO);
        authUser.setPassword(bCryptPasswordEncoder().encode(createAuthUserDTO.getPassword()));
        authUser.setRules(Set.of(rule));
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    public List<AuthUserDTO> findAll() {
        return authRepository.findAll()
                .stream()
                .map(AuthUserDTO::convert)
                .collect(Collectors.toList());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
