package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.OrderPaymentDTO;
import br.com.daciosoftware.shop.models.entity.order.OrderPayment;
import br.com.daciosoftware.shop.order.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    public List<OrderPaymentDTO> findByOrderId(Long orderId) {
        return orderPaymentRepository.findByOrderId(orderId).stream()
                .map(OrderPaymentDTO::convert)
                .sorted(Comparator.comparing(OrderPaymentDTO::getDatePayment).reversed())
                .collect(Collectors.toList());
    }

    public void save(OrderPaymentDTO orderPaymentDTO) {
        orderPaymentRepository.save(OrderPayment.convert(orderPaymentDTO));
    }
}
