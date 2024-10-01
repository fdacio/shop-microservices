package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDTO {
    private String token;
    private Long expireToken;
}
