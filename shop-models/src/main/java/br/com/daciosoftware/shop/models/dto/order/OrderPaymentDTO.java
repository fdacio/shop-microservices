package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.entity.order.OrderPayment;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPaymentDTO {
    private Long id;
    private LocalDateTime datePayment;
    private OrderStatus status;
    private String message;
    private Long orderId;
    private Long credcardId;

    public static OrderPaymentDTO convert(OrderPayment orderPayment) {
        OrderPaymentDTO orderPaymentDTO = new OrderPaymentDTO();
        orderPaymentDTO.setId(orderPayment.getId());
        orderPaymentDTO.setDatePayment(orderPayment.getDatePayment());
        orderPaymentDTO.setStatus(orderPayment.getStatus());
        orderPaymentDTO.setMessage(orderPayment.getMessage());
        orderPaymentDTO.setOrderId(orderPayment.getOrderId());
        orderPaymentDTO.setCredcardId(orderPayment.getCredcardId());
        return orderPaymentDTO;
    }
}
