package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.*;
import br.com.daciosoftware.shop.models.dto.auth.*;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private TokenService tokenConfig;
    @Autowired
    private CustomerService customerService;

    public TokenDTO login(LoginDTO loginDTO) {
        AuthUser user = authRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(AuthUserNotFoundException::new);

        boolean loginValid = bCryptPasswordEncoder().matches(loginDTO.getPassword(), user.getPassword());
        if (!loginValid) throw new AuthInvalidLoginException();

        AuthUserDTO authUserDTO = AuthUserDTO.convert(user);
        return tokenConfig.getToken(authUserDTO);

    }

    public AuthUserDTO findAuthenticatedUser(String token) {
        String keyToken = getKeyTokenIdUserByTokenJWT(token.replace("Bearer ", ""));
        AuthUser authUser = authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        return AuthUserDTO.convert(authUser);
    }

    public TokenDTO refreshToken(TokenDTO tokenExpired) {
        String keyToken = getKeyTokenIdUserByTokenJWT(tokenExpired.getToken());
        AuthUser authUser = authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        return tokenConfig.getToken(AuthUserDTO.convert(authUser));
    }

    public AuthUserDTO createOperatorUser(CreateAuthUserDTO createAuthUserDTO) {
        RuleDTO rule = ruleService.findByNome(RuleEnum.OPERATOR.getName());
        return createUser(createAuthUserDTO, rule);
    }

    public AuthUserDTO createAdminUser(CreateAuthUserDTO createAuthUserDTO) {
        RuleDTO rule = ruleService.findByNome(RuleEnum.ADMIN.getName());
        return createUser(createAuthUserDTO, rule);
    }

    public AuthUserDTO createUserFromCustomer(CreateAuthUserDTO createAuthUserDTO) {
        RuleDTO rule = ruleService.findByNome(RuleEnum.CUSTOMER.getName());
        return createUser(createAuthUserDTO, rule);
    }

    @Transactional
    public CustomerDTO createCustomerFromAuthUser(Long userId, CustomerDTO customerDTO) {
        AuthUser authUser = authRepository.findById(userId).orElseThrow(AuthUserNotFoundException::new);
        validKeyTokenExists(authUser.getKeyToken());
        RuleDTO ruleCustomer = ruleService.findByNome(RuleEnum.CUSTOMER.getName());
        authUser.getRules().add(Rule.convert(ruleCustomer));
        authRepository.save(authUser);
        customerDTO.setKeyAuth(authUser.getKeyToken());
        return customerService.createCustomer(customerDTO);
    }

    private AuthUserDTO createUser(CreateAuthUserDTO createAuthUserDTO, RuleDTO ruleDTO) {
        validUsernameUnique(createAuthUserDTO.getUsername(), null);
        validEmailUnique(createAuthUserDTO.getEmail(), null);
        AuthUser authUser = AuthUser.convert(createAuthUserDTO);
        authUser.setPassword(bCryptPasswordEncoder().encode(createAuthUserDTO.getPassword()));
        authUser.setKeyToken(geraKeyTokenForCreateUser(createAuthUserDTO.getUsername()));
        authUser.setRules(Set.of(Rule.convert(ruleDTO)));
        authUser.setDataCadastro(LocalDateTime.now());
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    public List<AuthUserDTO> findAll() {
        return authRepository.findAll()
                .stream()
                .map(AuthUserDTO::convert)
                .sorted(Comparator.comparing(AuthUserDTO::getNome))
                .collect(Collectors.toList());
    }

    public AuthUserDTO findById(Long userId) {
        return authRepository.findById(userId)
                .map(AuthUserDTO::convert)
                .orElseThrow(AuthUserNotFoundException::new);
    }

    public AuthUserDTO findByKeyToken(String keyToken) {
        return authRepository.findByKeyToken(keyToken).map(AuthUserDTO::convert).orElseThrow(AuthUserNotFoundException::new);
    }

    public List<AuthUserDTO> findByRule(Long ruleId) {
        RuleDTO rule = ruleService.findById(ruleId);
        List<AuthUserDTO> allUsers = authRepository.findAll().stream().map(AuthUserDTO::convert).toList();
        return allUsers.stream().filter(u -> u.getRules().contains(rule)).toList();
    }

    public AuthUserDTO findByUsername(String userName) {
        return authRepository.findByUsername(userName)
                .map(AuthUserDTO::convert)
                .orElseThrow(AuthUserNotFoundException::new);
    }

    @Transactional
    public void delete(Long userId) {
        authRepository.delete(AuthUser.convert(findById(userId)));
    }

    @Transactional
    public AuthUserDTO update(Long id, UpdateAuthUserDTO updateAuthUserDTO) {
        AuthUserDTO authUserDTO = findById(id);

        validUsernameUnique(updateAuthUserDTO.getUsername(), id);
        validEmailUnique(updateAuthUserDTO.getEmail(), id);

        boolean isUpdateNome = updateAuthUserDTO.getNome() != null && !updateAuthUserDTO.getNome().equals(authUserDTO.getNome());
        boolean isUpdateUsername = updateAuthUserDTO.getUsername() != null && !updateAuthUserDTO.getUsername().equals(authUserDTO.getUsername());
        boolean isUpdateEmail = updateAuthUserDTO.getEmail() != null && !updateAuthUserDTO.getEmail().equals(authUserDTO.getEmail());

        if (isUpdateNome) {
            authUserDTO.setNome(updateAuthUserDTO.getNome());
        }
        if (isUpdateUsername) {
            authUserDTO.setUsername(updateAuthUserDTO.getUsername());
        }
        if (isUpdateEmail) {
            authUserDTO.setEmail(updateAuthUserDTO.getEmail());
        }

        AuthUser authUser = AuthUser.convert(authUserDTO);

        authRepository.update(authUser);

        return AuthUserDTO.convert(authUser);

    }

    public AuthUserDTO updatePassword(PasswordDTO newPassword, String token) {
        if (!newPassword.getPassword().equals(newPassword.getRePassword())) {
            throw new AuthPasswordNotMatchException();
        }
        String keyToken = getKeyTokenIdUserByTokenJWT(token);
        AuthUser authUser = authRepository.findByKeyToken(keyToken).orElseThrow(AuthUserNotFoundException::new);
        authUser.setPassword(bCryptPasswordEncoder().encode(newPassword.getPassword()));
        return AuthUserDTO.convert(authRepository.save(authUser));
    }

    /* Private Methods */
    /**
     * Método utilizado para obter o KeyToken (campo identificador do AuthUser),
     * a partir do JWT Token
     * */
    private String getKeyTokenIdUserByTokenJWT(String token) {
        try {
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            TokenPayloadDTO payloadDTO = objectMapper.readValue(payload, TokenPayloadDTO.class);
            return payloadDTO.getSub();
        } catch (JsonProcessingException e) {
            throw new AuthUserInvalidKeyTokenException();
        }
    }

    /**
     * Método utilizado para criptografar o password do AuthUser
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Método utilizado para gerar o KeyToken(campo identificador do AuthUser)
     * através da classe UUID a partir de uma String, que no caso é o username
     * do AuthUser
     * */
    private String geraKeyTokenForCreateUser(String username) {
        return UUID.nameUUIDFromBytes(username.getBytes()).toString();
    }

    private void validUsernameUnique(String username, Long id) {
        Optional<AuthUserDTO> authUserDTO = authRepository.findByUsername(username).map(AuthUserDTO::convert);
        if (authUserDTO.isPresent()) {
            if (id == null) {
                throw new AuthUserUsernameExistsException();
            } else if (!id.equals(authUserDTO.get().getId())) {
                throw new AuthUserUsernameExistsException();
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

    private void validKeyTokenExists(String keyToken) {
        Optional<CustomerDTO> customerOptional = customerService.findByKeyAuth(keyToken);
        if (customerOptional.isPresent()) {
            throw new AuthUserCustomerConflictException();
        }
    }

}
