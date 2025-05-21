package org.example.webshop.Controller;

import org.example.webshop.Dtos.ProductDto;
import org.example.webshop.Model.Category;
import org.example.webshop.Model.Product;
import org.example.webshop.Services.CategoryService;
import org.example.webshop.Services.ProductServices;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServices productServices;
    private final CategoryService categoryServices;

    public ProductController(ProductServices productServices, CategoryService categoryService) {
        this.productServices = productServices;
        this.categoryServices=categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ordering,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Sort sort = Sort.by("id"); // Default sorting by ID
            if (ordering != null) {
                if (ordering.startsWith("-")) {
                    sort = Sort.by(ordering.substring(1)).descending(); // Descending order
                } else {
                    sort = Sort.by(ordering).ascending(); // Ascending order
                }
            }
            // Create a Pageable object with sorting
            Pageable pageable = PageRequest.of(page, size, sort);

            // Fetch products with filters
            Page<ProductDto> products = productServices.GetFilterdProducts(category, search, pageable);

            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    /*
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        try {
            Category category = new Category();
            category.setId(productDto.getCategoryId());

            ProductDto createdProduct = productServices.Addproduct(productDto.toEntity(category));
            return ResponseEntity.status(201).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        try {
            // Set the product ID to ensure the correct product is updated
            productDto.setId(id);

            // Call the service to update the product
            ProductDto updatedProduct = productServices.UpdateProduct(productDto);

            return ResponseEntity.ok(updatedProduct); // Return the updated product
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Return 404 if the product is not found
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Return 500 for other errors
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productServices.DeleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        try {
            ProductDto product = productServices.GetProduct(id);
            return ResponseEntity.status(201).body(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductDto[]> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            Category category = new Category(); // Assuming Category is an entity
            category.setId(categoryId); // Set the category ID
            ProductDto[] products = productServices.GetProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDto> addProduct(
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "picture", required = false) MultipartFile picture) {
        try {
            System.out.println("Received ProductDto: " + productDto);
            System.out.println("Picture is present: " + (picture != null));

            // Check if a picture is provided
            if (picture != null && !picture.isEmpty()) {
                // Save the picture to the project directory
                String picturePath = savePicture(picture, productDto.getId());
                // Set the picture path in the DTO
                productDto.setPicturePath(picturePath);
            }

            // Fetch the category and save the product
            Category category = categoryServices.getCategoryById(productDto.getCategoryId());
            if (category == null) {
                return ResponseEntity.status(404).body(null); // Return 404 if category not found
            }
            Product pdto= productDto.toEntity(category);
            ProductDto createdProduct = productServices.Addproduct(pdto);

            return ResponseEntity.status(201).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private String savePicture(MultipartFile picture, Long prodId) throws Exception {
        String uploadDir = "uploads"; // Directory to save pictures
        String fileName = "picture" + prodId;
        // String fileName = prodId + "_" + picture.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Ensure the directory exists
        Files.createDirectories(filePath.getParent());

        // Save the file
        Files.write(filePath, picture.getBytes());

        return filePath.toString();
    }
}
