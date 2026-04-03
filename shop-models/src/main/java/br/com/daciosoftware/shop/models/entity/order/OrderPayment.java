package br.com.daciosoftware.shop.models.entity.order;

import br.com.daciosoftware.shop.models.dto.order.OrderPaymentDTO;
import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "orders_payments")
@Table(name = "orders_payments", schema = "orders")
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime datePayment;
    private OrderStatus status;
    private String message;
    private Long orderId;
    private Long credcardId;

    public static OrderPayment convert(OrderPaymentDTO orderPaymentDTO) {
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setId(orderPaymentDTO.getId());
        orderPayment.setDatePayment(orderPaymentDTO.getDatePayment());
        orderPayment.setStatus(orderPaymentDTO.getStatus());
        orderPayment.setMessage(orderPaymentDTO.getMessage());
        orderPayment.setOrderId(orderPaymentDTO.getOrderId());
        orderPayment.setCredcardId(orderPaymentDTO.getCredcardId());
        return orderPayment;
    }

}
