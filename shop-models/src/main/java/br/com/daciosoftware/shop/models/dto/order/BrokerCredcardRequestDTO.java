package br.com.daciosoftware.shop.models.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerCredcardRequestDTO {
    private Long orderId;
    private String cpf;
    private String numberCard;
    private Integer cvv;
    private Float value;

    @Override
    public String toString() {
        return "{" +
                "\"orderId\":" + orderId +
                ",\"cpf\":\"" + cpf + "\"" +
                ",\"numberCard\":\"" + numberCard + "\"" +
                ",\"cvv\":" + cvv +
                ",\"value\":" + value +
                "}";
    }
}
