package org.example.webshop.Controller;

import org.example.webshop.Dtos.CategoryDto;
import org.example.webshop.Model.Category;
import org.example.webshop.Repository.CategoryRepository;
import org.example.webshop.Services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ordering) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(ordering != null ? ordering : "id"));
            Page<CategoryDto> categories = categoryService.GetAllCategories(pageable);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto createdCategory = categoryService.AddCategory(categoryDto.toEntity());
            return ResponseEntity.status(201).body(createdCategory);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryDto) {
        try {
            // Set the category ID to ensure the correct category is updated
            categoryDto.setId(id);

            // Call the service to update the category
            CategoryDto updatedCategory = categoryService.UpdateCategory(categoryDto);

            return ResponseEntity.ok(updatedCategory); // Return the updated category
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Return 404 if the category is not found
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Return 500 for other errors
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.DeleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}