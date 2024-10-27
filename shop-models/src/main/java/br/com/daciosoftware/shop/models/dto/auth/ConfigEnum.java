package br.com.daciosoftware.shop.models.dto.auth;

import lombok.Getter;

@Getter
public enum ConfigEnum {
    EXPIRE_TOKEN("EXPIRE_TOKEN");

    private final String chave;

    ConfigEnum(String chave) {
        this.chave = chave;
    }

}
