package br.com.daciosoftware.shop.models.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BrokerPaymentResponseDTO {
    private Long orderId;
    private Long credcardId;
    private Boolean authorized;
    private int codeResponse;
    private String message;
}
