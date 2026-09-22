package com.Gestion.d.un.Inventaire.de.Produits.repository;

import com.Gestion.d.un.Inventaire.de.Produits.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByLowStockAlertTrue();

    List<Product> findByQuantityInStockLessThan(Integer threshold);
}
