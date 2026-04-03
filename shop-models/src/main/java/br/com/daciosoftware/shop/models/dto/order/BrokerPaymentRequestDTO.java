package br.com.daciosoftware.shop.models.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerPaymentRequestDTO {
    private Long orderId;
    private Long credcardId;
    private String cpf;
    private String numberCard;
    private Integer cvv;
    private Float value;

    @Override
    public String toString() {
        return "{" +
                "\"orderId\":" + orderId +
                "\"credcardId\":" + credcardId +
                ",\"cpf\":\"" + cpf + "\"" +
                ",\"numberCard\":\"" + numberCard + "\"" +
                ",\"cvv\":" + cvv +
                ",\"value\":" + value +
                "}";
    }
}
