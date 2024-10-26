package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderReportRepository {

	@Query("select o from order o where o.customer.id = :customerId")
	List<Order> findByCustomerIdentifier(Long customerId);
}
