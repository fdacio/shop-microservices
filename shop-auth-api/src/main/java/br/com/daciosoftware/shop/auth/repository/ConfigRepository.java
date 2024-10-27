package br.com.daciosoftware.shop.auth.repository;

import br.com.daciosoftware.shop.models.entity.auth.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<Config> findByChave(String chave);

}
