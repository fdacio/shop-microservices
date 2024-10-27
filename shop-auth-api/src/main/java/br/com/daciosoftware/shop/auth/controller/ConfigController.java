package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.ConfigService;
import br.com.daciosoftware.shop.models.dto.auth.ConfigDTO;
import br.com.daciosoftware.shop.models.dto.auth.ConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("/config")
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigDTO> findAll() {
        return configService.findAll();
    }

    @PostMapping("/config")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigDTO save(@RequestBody ConfigDTO configDTO) {
        return configService.save(configDTO);
    }

    @PatchMapping("/config/{chave}")
    @ResponseStatus(HttpStatus.OK)
    public ConfigDTO update(@PathVariable String chave, @RequestBody ConfigDTO configDTO) {
        ConfigDTO config = configService.findByChave(chave);
        if (!configDTO.getValor().equals(config.getValor())) {
            config.setValor(configDTO.getValor());
        }
        return configService.save(config);
    }

    @GetMapping("/config/expire-token")
    @ResponseStatus(HttpStatus.OK)
    public ConfigDTO findExpireToken() {
        return configService.findByChave(ConfigEnum.EXPIRE_TOKEN.getChave());
    }

    @PatchMapping("/config/expire-token")
    @ResponseStatus(HttpStatus.OK)
    public ConfigDTO updateExpireToken(@RequestBody ConfigDTO configDTO) {
        return update(ConfigEnum.EXPIRE_TOKEN.getChave(), configDTO);
    }

}
