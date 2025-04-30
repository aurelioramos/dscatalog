package br.edu.ifms.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.dscatalog.dto.CategoryDTO;
import br.edu.ifms.dscatalog.repositories.CategoryRepository;

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
}