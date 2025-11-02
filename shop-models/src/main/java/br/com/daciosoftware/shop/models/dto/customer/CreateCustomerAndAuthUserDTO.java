package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCustomerAndAuthUserDTO {
    @Valid
    private CustomerDTO customer;
    @Valid
    private PasswordDTO password;
}
