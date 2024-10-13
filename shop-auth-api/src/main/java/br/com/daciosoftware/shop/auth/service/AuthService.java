package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.component.RsaKey;
import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.*;
import br.com.daciosoftware.shop.models.dto.auth.*;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private TokenService tokenConfig;
    @Autowired
    private CustomerService customerService;

    private String getKeyTokenFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(new RsaKey().getRsaPrivateKey())
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new AuthExpiredTokenException();
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
        if (!loginValid) throw new AuthInvalidLoginException();

        AuthUserDTO authUserDTO = AuthUserDTO.convert(user);
        return tokenConfig.getToken(authUserDTO);

    }

    public AuthUserDTO createUser(CreateAuthUserDTO createAuthUserDTO) {
        validUsernameUnique(createAuthUserDTO.getUsername(), null);
        validEmailUnique(createAuthUserDTO.getEmail(), null);
        Rule rule = ruleRepository.findByNome(RuleEnum.BASIC.getName()).orElseThrow(() -> new AuthRuleNotFoundException(RuleEnum.BASIC.getName()));
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

    public AuthUserDTO update(Long authUserId, UpdateAuthUserDTO updateAuthUserDTO) {
        AuthUserDTO authUserDTO = findById(authUserId);
        validUsernameUnique(updateAuthUserDTO.getUsername(), authUserId);
        validEmailUnique(updateAuthUserDTO.getEmail(), authUserId);
        return authUserDTO;
    }

    public void delete(Long userId) {
        authRepository.delete(AuthUser.convert(findById(userId)));
    }

    public AuthUserDTO updatePassword(PasswordDTO newPassword, String token) {
        if (!newPassword.getPassword().equals(newPassword.getRePassword())) {
            throw new AuthPasswordNotMatchException();
        }
        String keyToken = getKeyTokenFromToken(token);
        AuthUser authUser = authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        authUser.setPassword(bCryptPasswordEncoder().encode(newPassword.getPassword()));
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    public AuthUserDTO findAuthenticatedUser(String token) {
        String keyToken = getKeyTokenFromToken(token);
        AuthUser authUser = authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        return AuthUserDTO.convert(authUser);
    }

    public AuthUserDTO createUserFromCustomer(CreateAuthUserDTO createAuthUserDTO) {
        validUsernameUnique(createAuthUserDTO.getUsername(), null);
        validEmailUnique(createAuthUserDTO.getEmail(), null);
        Rule rule = ruleRepository.findByNome(RuleEnum.CUSTOMER.getName()).orElseThrow(() -> new AuthRuleNotFoundException(RuleEnum.CUSTOMER.getName()));
        AuthUser authUser = AuthUser.convert(createAuthUserDTO);
        authUser.setPassword(bCryptPasswordEncoder().encode(createAuthUserDTO.getPassword()));
        authUser.setKeyToken(geraKeyTokenForCreateUser(createAuthUserDTO.getUsername()));
        authUser.setRules(Set.of(rule));
        authUser.setDataCadastro(LocalDateTime.now());
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    @Transactional
    public CustomerDTO createCustomerFromAuthUser(Long userId, CustomerDTO customerDTO) {
        validCreateCustomerFromAuthUser(customerDTO);
        Rule ruleCustomer = ruleRepository.findByNome(RuleEnum.CUSTOMER.getName()).orElseThrow(() -> new AuthRuleNotFoundException(RuleEnum.CUSTOMER.getName()));
        AuthUser authUser = authRepository.findById(userId).orElseThrow(AuthUserNotFoundException::new);
        authUser.getRules().add(ruleCustomer);
        authRepository.save(authUser);
        customerDTO.setKeyAuth(authUser.getKeyToken());
        return customerService.createCustomer(customerDTO);
    }

    private void validUsernameUnique(String username, Long id) {
        Optional<AuthUserDTO> authUserDTO = authRepository.findByUsername(username).map(AuthUserDTO::convert);
        if (authUserDTO.isPresent()) {
            if (id == null) {
                throw new AuthUsernameExistsException();
            } else if (!id.equals(authUserDTO.get().getId())) {
                throw new AuthUsernameExistsException();
            }
        }
    }

    private void validEmailUnique(String email, Long id) {
        Optional<AuthUserDTO> authUserDTO = authRepository.findByEmail(email).map(AuthUserDTO::convert);
        if (authUserDTO.isPresent()) {
            if (id == null) {
                throw new AuthEmailExistsException();
            } else if (!id.equals(authUserDTO.get().getId())) {
                throw new AuthEmailExistsException();
            }
        }
    }

    private void validCreateCustomerFromAuthUser(CustomerDTO customerDTO) {

        if (customerDTO.getKeyAuth() != null && !customerDTO.getKeyAuth().isEmpty()) {
            throw new AuthUserCustomerConflictException();
        }
    }

}
