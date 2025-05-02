package br.edu.ifms.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.edu.ifms.dscatalog.entities.Product;
import br.edu.ifms.dscatalog.repositories.CategoryRepository;
import br.edu.ifms.dscatalog.repositories.ProductRepository;
import br.edu.ifms.dscatalog.services.exceptions.DatabaseException;
import br.edu.ifms.dscatalog.services.exceptions.ResourceNotFoundException;
import br.edu.ifms.dscatalog.tests.Factory;

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
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        idReferencedByCategory = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.save( ArgumentMatchers.any())).thenReturn(product);

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(new DataIntegrityViolationException("integrity constraint violation")).when(productRepository)
                .deleteById(idReferencedByCategory);
    }

    @Test
    void findAllPagedShouldReturnPageWhenIdExists() {

        Pageable pageable = PageRequest.of(0, 10);
        var result = service.findAllPaged(pageable);

        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
        Assertions.assertNotNull(result);
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