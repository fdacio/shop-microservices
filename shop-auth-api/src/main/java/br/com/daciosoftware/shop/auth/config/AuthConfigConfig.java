package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.auth.repository.ConfigRepository;
import br.com.daciosoftware.shop.models.dto.auth.ConfigEnum;
import br.com.daciosoftware.shop.models.entity.auth.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class AuthConfigConfig implements CommandLineRunner {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public void run(String... args) throws Exception {

        Optional<Config> configOptional = configRepository.findByChave(ConfigEnum.EXPIRE_TOKEN.getChave());
        if (configOptional.isEmpty()) {
            Config config = new Config();
            config.setChave(ConfigEnum.EXPIRE_TOKEN.getChave());
            config.setValor("300");
            configRepository.save(config);
            System.out.println("Configuração de EXPIRE_TOKEN criada como sucesso!");

        }
    }
}
