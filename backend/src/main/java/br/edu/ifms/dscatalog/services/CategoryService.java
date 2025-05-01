package br.edu.ifms.dscatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.dscatalog.dto.CategoryDTO;
import br.edu.ifms.dscatalog.entities.Category;
import br.edu.ifms.dscatalog.repositories.CategoryRepository;
import br.edu.ifms.dscatalog.services.exceptions.DatabaseException;
import br.edu.ifms.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        Page<Category> list = repository.findAll(pageable);
        return list.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        var result = repository.findById(id);
        return new CategoryDTO(result.orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Category with ID " + id + "was not found.");
        }
    }

    public void delete(Long id) {
        try {
            // If the entity is not found in the persistence store it is silently ignored.
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Category with ID " + id + "was not found.");
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException(
                    "Category with ID " + id + " cannot be deleted due to an integrity constraint violation.");
        }
    }   
}