package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.entity.order.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {
    List<OrderPayment> findByOrderId(Long orderId);
}
