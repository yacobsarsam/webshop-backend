package org.example.webshop.Services;

import org.example.webshop.Dtos.CategoryDto;
import org.example.webshop.Model.Category;
import org.example.webshop.Repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<CategoryDto> GetAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryDto::fromEntity);
    }

    @Override
    public CategoryDto AddCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return CategoryDto.fromEntity(savedCategory);
    }

    @Override
    public void DeleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}
