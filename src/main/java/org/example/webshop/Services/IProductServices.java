package org.example.webshop.Services;

import org.example.webshop.Dtos.ProductDto;
import org.example.webshop.Model.Category;
import org.example.webshop.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductServices {
    Page<ProductDto> GetFilterdProducts(Long categoryId, String search, Pageable pageable);
    List<ProductDto> GetProducts();
    ProductDto GetProduct(long id);
    ProductDto Addproduct(Product product);
    void DeleteProduct(Long id);
    ProductDto[] GetProductsByCategory(Category category);
}
