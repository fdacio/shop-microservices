package br.com.daciosoftware.shop.models.dto.auth;

import lombok.Getter;

@Getter
public enum RuleEnum {
    ADMIN("Admin"),
    BASIC("Basic"),
    CUSTOMER("Customer");

    private final String name;

    RuleEnum(String name) {
        this.name = name;
    }

}
