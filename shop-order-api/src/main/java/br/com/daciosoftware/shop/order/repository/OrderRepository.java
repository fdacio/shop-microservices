package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.entity.order.Order;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from orders o where o.customer.id = :customerId")
	List<Order> findByCustomerIdentifier(Long customerId);

	@Modifying
	@Query("update orders set status = :status where id = :id")
	void updateStatus(Long id, OrderStatus status);
}
