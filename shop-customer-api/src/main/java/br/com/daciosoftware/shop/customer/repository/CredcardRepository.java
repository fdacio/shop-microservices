package br.com.daciosoftware.shop.customer.repository;

import br.com.daciosoftware.shop.models.entity.customer.Credcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredcardRepository extends JpaRepository<Credcard, Long> {
    List<Credcard> findByCustomerId(Long customerId);
}
