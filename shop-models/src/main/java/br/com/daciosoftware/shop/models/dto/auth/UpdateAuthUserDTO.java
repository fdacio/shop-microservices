package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateAuthUserDTO {

    private String nome;
    private String username;
    private String email;
}
