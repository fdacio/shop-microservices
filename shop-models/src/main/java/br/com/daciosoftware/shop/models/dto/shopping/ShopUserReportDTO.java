package br.com.daciosoftware.shop.models.dto.shopping;

import br.com.daciosoftware.shop.models.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserReportDTO {

    UserDTO user;
    Integer count;
    Float total;
}
