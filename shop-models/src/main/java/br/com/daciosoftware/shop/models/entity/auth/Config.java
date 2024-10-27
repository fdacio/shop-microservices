package br.com.daciosoftware.shop.models.entity.auth;

import br.com.daciosoftware.shop.models.dto.auth.ConfigDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "config")
@Table(name = "config", schema = "auth")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String chave;
    private String valor;

    public static Config convert(ConfigDTO configDTO) {
        Config config = new Config();
        config.setId(configDTO.getId());
        config.setChave(configDTO.getChave());
        config.setValor(configDTO.getValor());
        return config;
    }


}
