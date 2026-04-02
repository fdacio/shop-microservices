package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredcardShotDTO {
    private Long id;
    private String numberCard;
    private Boolean principal;
    private CustomerShotDTO customer;

    public static CredcardShotDTO convert(Credcard credcard) {
        return new CredcardShotDTO(
                credcard.getId(),
                credcard.getNumberCard(),
                credcard.getPrincipal(),
                CustomerShotDTO.convert(credcard.getCustomer())
        );
    }
}
