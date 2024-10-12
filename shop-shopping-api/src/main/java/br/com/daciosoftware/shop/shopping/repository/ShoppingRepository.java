package br.com.daciosoftware.shop.shopping.repository;

import br.com.daciosoftware.shop.models.entity.shopping.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShoppingRepository extends JpaRepository<Shop, Long>, ShoppingReportRepository {

	@Query("select s from shop s where s.customer.id = :customerId")
	List<Shop> findByCustomerIdentifier(Long customerId);
}
