package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCustomerAndAuthUserDTO {

    @NotNull(message = "Informe os dados do cliente")
    @Valid
    private CustomerDTO customer;
    @NotNull(message = "Informe a senha e a confirmação de senha")
    @Valid
    private PasswordDTO password;
}
