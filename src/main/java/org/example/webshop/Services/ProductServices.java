package org.example.webshop.Services;

import org.example.webshop.Dtos.ProductDto;
import org.example.webshop.Model.Category;
import org.example.webshop.Model.Product;
import org.example.webshop.Repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServices implements IProductServices{
    private final ProductRepository productRepository;

    public ProductServices(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDto> GetFilterdProducts(Long categoryId, String search, Pageable pageable) {
        if (categoryId != null && search != null) {
            return productRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, search, pageable)
                    .map(ProductDto::fromEntity);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable)
                    .map(ProductDto::fromEntity);
        } else if (search != null) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable)
                    .map(ProductDto::fromEntity);
        } else {
            return productRepository.findAll(pageable)
                    .map(ProductDto::fromEntity);
        }
    }

    @Override
    public List<ProductDto> GetProducts(){
        return productRepository.findAll()
                .stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    @Override
    public ProductDto GetProduct(long id) {
        return productRepository.findById(id)
                .map(ProductDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public ProductDto Addproduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return ProductDto.fromEntity(savedProduct);
    }

    @Override
    public void DeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public ProductDto[] GetProductsByCategory(Category category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(ProductDto::fromEntity)
                .toArray(ProductDto[]::new);
    }

    @Override
    public ProductDto UpdateProduct(ProductDto productDto) {
        // Find the existing product by ID
        Product existingProduct = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productDto.getId()));

        // Update the fields of the existing product
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setCategory(new Category(productDto.getCategoryId())); // Assuming Category is set by ID
        existingProduct.setPicturePath(productDto.getPicturePath());

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Convert the updated product to a DTO and return it
        return ProductDto.fromEntity(updatedProduct);
    }
}
