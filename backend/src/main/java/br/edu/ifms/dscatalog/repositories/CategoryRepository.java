package br.edu.ifms.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifms.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}