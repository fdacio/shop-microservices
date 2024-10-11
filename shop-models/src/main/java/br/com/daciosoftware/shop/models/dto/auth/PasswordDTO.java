package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordDTO {

    private String password;
    private String rePassword;
}
