package ru.jordan.food_storage.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.model.Product;
import ru.jordan.food_storage.repository.ProductRepository;
import ru.jordan.food_storage.service.product.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames={"product"})
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Product createProduct(ProductDto productDto) {
        if (productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Категория с таким именем уже существует");
        }

        Product Product = modelMapper.map(productDto, Product.class);

        return productRepository.save(Product);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Optional<Product> ProductOptional = productRepository.findById(id);
        return ProductOptional.orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Product updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            modelMapper.map(productDto, existingProduct);

            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("Product with id " + productDto.getId() + " not found");
        }
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteProduct(Long id) {
        Optional<Product> ProductOptional = productRepository.findById(id);

        if (ProductOptional.isPresent()) {
            Product Product = ProductOptional.get();
            productRepository.delete(Product);
        } else {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<Product> findProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return productRepository.findAll();
        } else {
            return productRepository.findByNameContainingNative(name);
        }
    }
}
