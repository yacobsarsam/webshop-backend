package org.example.webshop.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.webshop.Model.Category;
import org.example.webshop.Model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String picturePath;
    private Long categoryId;

    public static ProductDto fromEntity(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getPicturePath(),
                product.getCategory().getId()
        );
    }

    public Product toEntity(Category category) {
        return new Product(
                this.id,
                this.name,
                this.description,
                this.price,
                this.quantity,
                this.picturePath,
                category
        );
    }
}