package br.edu.ifms.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.dscatalog.dto.CategoryDTO;
import br.edu.ifms.dscatalog.entities.Category;
import br.edu.ifms.dscatalog.repositories.CategoryRepository;
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
}