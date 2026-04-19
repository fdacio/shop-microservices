package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredcardShotDTO {
    private Long id;
    private String numberCard;
    private LocalDate valid;
    private Integer cvv;
    private Boolean principal;

    public static CredcardShotDTO convert(CredcardDTO dto) {
        CredcardShotDTO credcardShotDTO =    new CredcardShotDTO();
        credcardShotDTO.setId(dto.getId());
        credcardShotDTO.setNumberCard(dto.getNumberCard());
        credcardShotDTO.setValid(dto.getValid());
        credcardShotDTO.setCvv(dto.getCvv());
        credcardShotDTO.setPrincipal(dto.getPrincipal());
        return credcardShotDTO;
    }
}
