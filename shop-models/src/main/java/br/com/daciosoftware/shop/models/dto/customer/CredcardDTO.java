package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredcardDTO {
    private Long id;
    private String numberCard;
    private LocalDate valid;
    private Integer cvv;
    private Boolean principal;
    private CustomerDTO customer;

    public static CredcardDTO convert(Credcard credcard) {
        CredcardDTO credcardDTO = new CredcardDTO();
        credcardDTO.setId(credcard.getId());
        credcardDTO.setNumberCard(credcard.getNumberCard());
        credcardDTO.setValid(credcard.getValid());
        credcardDTO.setCvv(credcard.getCvv());
        credcardDTO.setPrincipal(credcard.getPrincipal());
        if (credcard.getCustomer() != null)
            credcardDTO.setCustomer(CustomerDTO.convert(credcard.getCustomer()));
        return credcardDTO;
    }
}
