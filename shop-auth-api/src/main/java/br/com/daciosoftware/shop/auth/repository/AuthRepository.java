package br.com.daciosoftware.shop.auth.repository;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByKeyToken(String keyToken);
}
