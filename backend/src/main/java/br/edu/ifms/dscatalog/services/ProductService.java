package br.edu.ifms.dscatalog.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.dscatalog.dto.CategoryDTO;
import br.edu.ifms.dscatalog.dto.ProductDTO;
import br.edu.ifms.dscatalog.entities.Category;
import br.edu.ifms.dscatalog.entities.Product;
import br.edu.ifms.dscatalog.repositories.CategoryRepository;
import br.edu.ifms.dscatalog.repositories.ProductRepository;
import br.edu.ifms.dscatalog.services.exceptions.DatabaseException;
import br.edu.ifms.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
    private ProductRepository repository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        var result = repository.findById(id);
        return new ProductDTO(result.orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Product with ID " + id + "was not found.");
        }
    }

    public void delete(Long id) {
        try {
            // If the entity is not found in the persistence store it is silently ignored.
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Product with ID " + id + "was not found.");
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException(
                    "Product with ID " + id + " cannot be deleted due to an integrity constraint violation.");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();

        for (CategoryDTO categoryDTO : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}