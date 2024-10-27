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

        configRepository.findByChave(ConfigEnum.EXPIRE_TOKEN.getChave()).ifPresentOrElse(
                (config) -> {
                    System.out.printf("Configuração Auth %s já existe\n", config.getChave());
                },
                () -> {
                    Config config = new Config();
                    config.setChave(ConfigEnum.EXPIRE_TOKEN.getChave());
                    config.setValor("300");
                    configRepository.save(config);
                }
        );



    }
}
