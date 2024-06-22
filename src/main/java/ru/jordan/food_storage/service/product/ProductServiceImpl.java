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
import ru.jordan.food_storage.mapper.ProductMapper;
import ru.jordan.food_storage.model.Product;
import ru.jordan.food_storage.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames={"product"})
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        if (productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Категория с таким именем уже существует");
        }

        Product Product = modelMapper.map(productDto, Product.class);
        Product saveProduct = productRepository.save(Product);

        return productMapper.productToProductDto(saveProduct);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Optional<Product> ProductOptional = productRepository.findById(id);
        return ProductOptional.map(productMapper::productToProductDto).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public ProductDto updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            modelMapper.map(productDto, existingProduct);
            Product updateProduct = productRepository.save(existingProduct);
            return productMapper.productToProductDto(updateProduct);
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
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<ProductDto> findProductsByName(String name) {
        List<Product> products;
        if (name == null || name.trim().isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByNameContainingNative(name);
        }
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }
}
