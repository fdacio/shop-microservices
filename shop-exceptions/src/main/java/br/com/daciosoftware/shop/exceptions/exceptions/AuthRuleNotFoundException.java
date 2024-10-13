package br.com.daciosoftware.shop.exceptions.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class AuthRuleNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7140798451972545956L;
    private final String ruleName;

    public AuthRuleNotFoundException(String ruleName) {
        this.ruleName = ruleName;
    }

}
