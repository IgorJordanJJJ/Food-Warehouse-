package ru.jordan.food_storage.facade.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.service.product.ProductServiceImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // Транзакция только для чтения
public class ProductFacadeImpl implements ProductFacade {

    private final ProductServiceImpl productService;

    @Override
    public ProductDto getProductById(Long id) {
        return productService.getProductById(id);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public ProductDto saveProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public ProductDto updateProduct(ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public List<ProductDto> findProductsByName(String name) {
        return productService.findProductsByName(name);
    }
}
