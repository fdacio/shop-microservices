package br.com.daciosoftware.shop.models.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthUserKeyTokenDTO {
    private Long id;
    private String username;
    private String keyToken;
}
