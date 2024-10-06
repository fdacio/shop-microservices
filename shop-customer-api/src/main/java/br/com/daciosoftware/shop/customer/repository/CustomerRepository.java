package br.com.daciosoftware.shop.customer.repository;

import br.com.daciosoftware.shop.models.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Optional<Customer> findByCpf(String cpf);
	List<Customer> findByNomeContainingIgnoreCase(String nome);
	Optional<Customer> findByEmail(String email);
}
