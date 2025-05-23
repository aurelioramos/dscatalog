package br.edu.ifms.dscatalog.dto;

import java.io.Serializable;

import br.edu.ifms.dscatalog.entities.Category;

public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}