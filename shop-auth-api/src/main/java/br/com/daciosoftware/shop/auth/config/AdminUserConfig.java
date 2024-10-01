package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.repository.RuleRepository;
import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import br.com.daciosoftware.shop.models.entity.auth.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {

        String nome = "Administrador";
        String userName = "admin@daciosoftware.com.br";
        String password = bCryptPasswordEncoder().encode("123456");
        String email = "fdacio@gmail.com";
        Set<Rule> rule = Set.of(ruleRepository.findByNome("Admin"));

        Optional<AuthUser> user = authRepository.findByUsername(userName);
        if (user.isEmpty()) {
            AuthUser admin = new AuthUser();
            admin.setId(1L);
            admin.setNome(nome);
            admin.setUsername(userName);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setRules(rule);
            admin.setDataCadastro(LocalDateTime.now());
            authRepository.save(admin);
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
