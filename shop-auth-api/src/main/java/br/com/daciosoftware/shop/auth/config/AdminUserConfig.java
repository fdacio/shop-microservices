package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.AuthRepository;
import br.com.daciosoftware.shop.auth.service.AuthService;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;


@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) {

        String nome = "Administrador";
        String userName = "admin@daciosoftware.com.br";
        String password = "123456";
        String email = "admin@daciosoftware.com.br";
        Optional<AuthUserDTO> optional = Optional.ofNullable(authService.findByUsername(userName));
        if (optional.isEmpty()) {
            CreateAuthUserDTO admin = new CreateAuthUserDTO();
            admin.setId(1L);
            admin.setNome(nome);
            admin.setUsername(userName);
            admin.setEmail(email);
            admin.setPassword(password);
            AuthUserDTO authUserDTO = authService.createAdminUser(admin);
            System.err.printf("Usuário %s criado com sucesso%n", authUserDTO.getNome());
        }

    }

}
