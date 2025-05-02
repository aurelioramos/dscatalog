package br.edu.ifms.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.edu.ifms.dscatalog.repositories.CategoryRepository;
import br.edu.ifms.dscatalog.repositories.ProductRepository;
import br.edu.ifms.dscatalog.services.exceptions.DatabaseException;
import br.edu.ifms.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long idReferencedByCategory;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        idReferencedByCategory = 3L;
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(new DataIntegrityViolationException("integrity constraint violation")).when(productRepository)
                .deleteById(idReferencedByCategory);
    }

    @Test
    void deleteShouldNotThrowExceptionWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(idReferencedByCategory);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(idReferencedByCategory);
    }

}