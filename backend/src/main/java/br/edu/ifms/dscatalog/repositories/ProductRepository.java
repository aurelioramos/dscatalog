package br.edu.ifms.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}