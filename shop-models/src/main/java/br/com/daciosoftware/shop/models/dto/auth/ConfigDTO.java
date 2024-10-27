package br.com.daciosoftware.shop.models.dto.auth;

import br.com.daciosoftware.shop.models.entity.auth.Config;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ConfigDTO {

    private Long id;
    private String chave;
    private String valor;

    public static ConfigDTO convert (Config config) {
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setId(config.getId());
        configDTO.setChave(config.getChave());
        configDTO.setValor(config.getValor());
        return  configDTO;
    }
}
