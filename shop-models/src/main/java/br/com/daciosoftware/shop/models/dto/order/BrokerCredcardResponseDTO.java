package br.com.daciosoftware.shop.models.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BrokerCredcardResponseDTO {
    private Boolean authorized;
    private String message;
}
