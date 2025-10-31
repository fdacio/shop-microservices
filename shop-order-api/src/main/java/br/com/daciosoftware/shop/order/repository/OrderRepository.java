package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from orders o where o.customer.id = :customerId")
	List<Order> findByCustomerIdentifier(Long customerId);
}
