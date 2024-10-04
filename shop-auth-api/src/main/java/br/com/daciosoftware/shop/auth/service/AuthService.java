package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.config.RsaKey;
import br.com.daciosoftware.shop.auth.config.TokenConfig;
import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthLoginErrorException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.*;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TokenConfig tokenConfig;


    private AuthUser getAuthUserFromToken(String token) {
        try {
            String keyToken = Jwts.parser()
                    .setSigningKey(new RsaKey().getPrivate())
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();
            return authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String geraKeyTokenForCreateUser(String value) {
        return UUID.nameUUIDFromBytes(value.getBytes()).toString();
    }

    public TokenDTO login(LoginDTO loginDTO) {
        AuthUser user = authRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(AuthUserNotFoundException::new);

        boolean loginValid = bCryptPasswordEncoder().matches(loginDTO.getPassword(), user.getPassword());
        if (!loginValid) throw new AuthLoginErrorException();

        AuthUserDTO authUserDTO = AuthUserDTO.convert(user);
        return tokenConfig.getToken(authUserDTO);

    }

    public AuthUserDTO createUser(CreateAuthUserDTO createAuthUserDTO) {
        Rule rule = ruleRepository.findByNome("Basic");
        AuthUser authUser = AuthUser.convert(createAuthUserDTO);
        authUser.setPassword(bCryptPasswordEncoder().encode(createAuthUserDTO.getPassword()));
        authUser.setKeyToken(geraKeyTokenForCreateUser(createAuthUserDTO.getUsername()));
        authUser.setRules(Set.of(rule));
        authUser.setDataCadastro(LocalDateTime.now());
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    public List<AuthUserDTO> findAll() {
        return authRepository.findAll()
                .stream()
                .map(AuthUserDTO::convert)
                .collect(Collectors.toList());
    }

    public AuthUserDTO findById(Long userId) {
        return authRepository.findById(userId)
                .map(AuthUserDTO::convert)
                .orElseThrow(AuthUserNotFoundException::new);
    }

    public AuthUserDTO findByUsername(String userName) {
        return authRepository.findByUsername(userName)
                .map(AuthUserDTO::convert)
                .orElseThrow(AuthUserNotFoundException::new);
    }

    public void delete(Long userId) {
        authRepository.delete(AuthUser.convert(findById(userId)));
    }

    public AuthUserDTO updatePassword(UpdatePasswordDTO password, String token) {
        AuthUser authUser = getAuthUserFromToken(token);
        authUser.setPassword(bCryptPasswordEncoder().encode(password.getNewPassword()));
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    public AuthUserDTO findAuthenticatedUser(String token) {
        return AuthUserDTO.convert(getAuthUserFromToken(token));
    }

}
