package br.com.daciosoftware.shop.models.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {

    @NotEmpty(message = "Informe o username")
    private String username;
    @NotEmpty(message = "Informe o password")
    private String password;
}
