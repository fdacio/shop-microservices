package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.repository.ConfigRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthConfigNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.ConfigDTO;
import br.com.daciosoftware.shop.models.entity.auth.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    public List<ConfigDTO> findAll() {
        return configRepository.findAll().stream().map(ConfigDTO::convert).collect(Collectors.toList());
    }

    public ConfigDTO findByChave(String chave) {
        return configRepository.findByChave(chave).map(ConfigDTO::convert).orElseThrow(AuthConfigNotFoundException::new);
    }

    public Optional<ConfigDTO> findOptionalByChave(String chave) {
        return configRepository.findByChave(chave).map(ConfigDTO::convert);
    }

    public ConfigDTO save(ConfigDTO configDTO) {
        Config config = Config.convert(configDTO);
        return ConfigDTO.convert(configRepository.save(config));
    }

}
