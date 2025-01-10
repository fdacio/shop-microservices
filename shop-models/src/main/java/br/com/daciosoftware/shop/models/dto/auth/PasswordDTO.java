package br.com.daciosoftware.shop.models.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordDTO {

    @NotBlank(message="Informe a senha")
    @Size(message="Senha tem que ter no máximo 255 caracteres", max = 255)
    private String password;
    @NotBlank(message="Confirme a senha")
    private String rePassword;
}
