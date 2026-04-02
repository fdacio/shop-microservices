package br.com.daciosoftware.shop.models.entity.customer;

import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "credcards")
@Table(name = "credcards", schema = "customers")
public class Credcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String numberCard;
    private LocalDate valid;
    private Boolean principal;
    private Integer cvv;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static Credcard convert(CredcardDTO credcardDTO) {
        Credcard credcard = new Credcard();
        credcard.setId(credcardDTO.getId());
        credcard.setNumberCard(credcardDTO.getNumberCard());
        credcard.setValid(credcardDTO.getValid());
        credcard.setCvv(credcardDTO.getCvv());
        credcard.setPrincipal(credcardDTO.getPrincipal());
        if (credcardDTO.getCustomer() != null)
            credcard.setCustomer(Customer.convert(credcardDTO.getCustomer()));
        return credcard;
    }

}
