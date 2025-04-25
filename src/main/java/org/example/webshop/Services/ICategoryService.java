package org.example.webshop.Services;

import org.example.webshop.Dtos.CategoryDto;
import org.example.webshop.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Page<CategoryDto> GetAllCategories(Pageable pageable);
    CategoryDto AddCategory(Category category);
    void DeleteCategory(Long id);
}