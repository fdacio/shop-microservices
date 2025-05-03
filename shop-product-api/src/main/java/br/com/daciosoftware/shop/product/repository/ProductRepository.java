package br.com.daciosoftware.shop.product.repository;

import br.com.daciosoftware.shop.models.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdentifier(String productIdentifier);

    List<Product> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    @Query(value = "select p from product p join category c on p.category.id = c.id where c.id = :categoryId")
    List<Product> findByCategory(@Param("categoryId") Long categoryId);
}
