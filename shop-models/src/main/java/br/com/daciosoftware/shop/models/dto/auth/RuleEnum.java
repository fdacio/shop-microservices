package br.com.daciosoftware.shop.models.dto.auth;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public enum RuleEnum {
    ADMIN(1L, "Admin"),
    OPERATOR(2L, "Operator"),
    CUSTOMER(3L, "Customer"),
    GUEST(4L, "Guest");

    private final String name;
    private final Long code;

    RuleEnum(Long code, String name) {
        this.code = code;
        this.name = name;
    }

}
