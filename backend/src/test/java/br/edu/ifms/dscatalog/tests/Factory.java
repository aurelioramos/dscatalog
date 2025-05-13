package br.edu.ifms.dscatalog.tests;

import java.time.Instant;

import br.edu.ifms.dscatalog.dto.ProductDTO;
import br.edu.ifms.dscatalog.entities.Category;
import br.edu.ifms.dscatalog.entities.Product;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png",
                Instant.parse("1970-01-01T00:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}