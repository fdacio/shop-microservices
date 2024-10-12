package br.com.daciosoftware.shop.models.dto.shopping;

import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopCustomerReportDTO {

    CustomerDTO customer;
    Integer count;
    Float total;
}
