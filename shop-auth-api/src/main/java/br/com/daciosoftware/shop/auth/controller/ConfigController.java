package br.com.daciosoftware.shop.auth.controller;

import br.com.daciosoftware.shop.auth.service.ConfigService;
import br.com.daciosoftware.shop.models.dto.auth.ConfigDTO;
import br.com.daciosoftware.shop.models.dto.auth.ConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigDTO save(@RequestBody ConfigDTO configDTO) {
        return configService.save(configDTO);
    }

    @PatchMapping("/{chave}")
    @ResponseStatus(HttpStatus.OK)
    public ConfigDTO update(@PathVariable String chave, @RequestBody ConfigDTO configDTO) {
        ConfigDTO config = configService.findByChave(chave);
        if (!configDTO.getValor().equals(config.getValor())) {
            config.setValor(configDTO.getValor());
        }
        return configService.save(config);
    }

    @PatchMapping("/update-expire-token")
    @ResponseStatus(HttpStatus.OK)
    public ConfigDTO updateExpireToken(@RequestBody ConfigDTO configDTO) {
        return update(ConfigEnum.EXPIRE_TOKEN.getChave(), configDTO);
    }

}
