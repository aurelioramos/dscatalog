package br.edu.ifms.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.dscatalog.dto.ProductDTO;
import br.edu.ifms.dscatalog.repositories.ProductRepository;

@SpringBootTest
@Transactional
class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        countTotalProducts = 25L;
    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists() {
        // When
        service.delete(existingId);
        // Then
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    void findAllPagedShouldReturnPageWhenPage0Size10() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        // When
        Page<ProductDTO> result = service.findAllPaged(pageRequest);
        // Then
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        // Given
        PageRequest pageRequest = PageRequest.of(50, 10);
        // When
        Page<ProductDTO> result = service.findAllPaged(pageRequest);
        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void findAllPagedShouldReturnOrderedPageWhenSortedByName() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        // When
        Page<ProductDTO> result = service.findAllPaged(pageRequest);
        // Then
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());

    }
}