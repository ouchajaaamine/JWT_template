package com.example.springsecurityjwttemplate.Services;

import com.example.springsecurityjwttemplate.model.Product;
import com.example.springsecurityjwttemplate.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> GetAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product AddProduct(Product product) {
        this.productRepository.save(product);

        return product;
    }

    @Override
    public Product GetProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product UpdateProduct(Long id, Product product) {
        Product existing = GetProductById(id);
        if (existing != null) {
            existing.setName(product.getName());
            existing.setPrice(product.getPrice());
            return productRepository.save(existing);
        }
        return null;
    }
    @Override
    public void DeleteProduct(Long id) {
                productRepository.deleteById(id);
    }
}
