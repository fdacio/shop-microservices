package br.com.daciosoftware.shop.user.repository;

import br.com.daciosoftware.shop.modelos.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByCpf(String cpf);
	List<User> findByNomeContainingIgnoreCase(String nome);
	Optional<User> findByIdAndKey(Long id, String key);
	Optional<User> findByEmail(String email);
}
