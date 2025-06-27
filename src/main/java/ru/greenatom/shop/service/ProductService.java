package ru.greenatom.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.greenatom.shop.dto.ProductDTO;
import ru.greenatom.shop.repository.entity.ProductEntity;
import ru.greenatom.shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> searchProducts(String name) {
        return productRepository.findByNameStartingWith(name);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductEntity saveProduct(ProductDTO dto) {
        return productRepository.save(new ProductEntity(dto));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Optional<ProductEntity> updateProduct(Long id, ProductDTO updatedProduct) {
        ProductEntity existingProductEntity = productRepository.getReferenceById(id);


        existingProductEntity.setName(updatedProduct.getName());
        existingProductEntity.setPrice(updatedProduct.getPrice());
        return Optional.of(productRepository.save(existingProductEntity));
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
