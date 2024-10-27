package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.exceptions.exceptions.AuthConfigNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.ConfigDTO;
import br.com.daciosoftware.shop.models.entity.auth.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigService {

    @Autowired
    private br.com.daciosoftware.shop.auth.repository.ConfigRepository configRepository;

    public List<ConfigDTO> findAll() {
        return configRepository.findAll().stream().map(ConfigDTO::convert).collect(Collectors.toList());
    }

    public ConfigDTO findByChave(String chave) {
        return configRepository.findByChave(chave).map(ConfigDTO::convert).orElseThrow(AuthConfigNotFoundException::new);
    }

    public ConfigDTO save(ConfigDTO configDTO) {
        Config config = Config.convert(configDTO);
        return ConfigDTO.convert(configRepository.save(config));
    }

}
