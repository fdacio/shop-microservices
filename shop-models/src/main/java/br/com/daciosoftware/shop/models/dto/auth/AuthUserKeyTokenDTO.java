package br.com.daciosoftware.shop.models.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserKeyTokenDTO {
    private Long id;
    private String username;
    private String keyToken;
}
