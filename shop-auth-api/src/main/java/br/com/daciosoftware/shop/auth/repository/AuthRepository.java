package br.com.daciosoftware.shop.auth.repository;

import br.com.daciosoftware.shop.models.entity.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByKeyToken(String keyToken);

    Optional<AuthUser> findByEmail(String email);

    @Modifying
    @Query("update authUser set " +
            "nome = :#{#pAuthUser.nome}, " +
            "username = :#{#pAuthUser.username}, " +
            "email = :#{#pAuthUser.email} " +
            "where id = :#{#pAuthUser.id}")
    void update(@Param(value="pAuthUser") AuthUser authUser);

}
