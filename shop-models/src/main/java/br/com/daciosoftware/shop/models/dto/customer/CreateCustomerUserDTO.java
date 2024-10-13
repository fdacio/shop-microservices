package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCustomerUserDTO {

    private CustomerDTO customer;
    private PasswordDTO password;
}
