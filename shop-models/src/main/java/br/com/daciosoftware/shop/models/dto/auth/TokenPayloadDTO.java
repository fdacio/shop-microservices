package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenPayloadDTO {
    private String iss;
    private String sub;
    private String exp;
    private String iat;
    private String scope;
}
