package br.edu.ifms.dscatalog.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import br.edu.ifms.dscatalog.entities.Product;
import br.edu.ifms.dscatalog.tests.Factory;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    long existingId;
    long nonExistingId;
    long countTotalProducts;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        var result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    void saveShouldThrowRuntimeExceptionWhenObjectIsNull() {

        Assertions.assertThrows(RuntimeException.class, () -> repository.save(null));
    }

    @Test
    void saveShouldThrowInvalidDataAccessApiUsageExceptionWhenObjectIsNull() {

        Assertions.assertThrowsExactly(InvalidDataAccessApiUsageException.class, () -> repository.save(null));
    }
}