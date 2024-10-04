package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {

        String nome = "Administrador";
        String userName = "admin@daciosoftware.com.br";
        String password = authService.bCryptPasswordEncoder().encode("123456");
        String email = "admin@daciosoftware.com.br";
        Set<Rule> rule = Set.of(ruleRepository.findByNome("Admin"));

        Optional<AuthUser> user = authRepository.findByUsername(userName);
        if (user.isEmpty()) {
            AuthUser admin = new AuthUser();
            admin.setId(1L);
            admin.setNome(nome);
            admin.setUsername(userName);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setKeyToken(authService.geraKeyTokenForCreateUser(userName));
            admin.setRules(rule);
            admin.setDataCadastro(LocalDateTime.now());
            authRepository.save(admin);
        }
    }

}
