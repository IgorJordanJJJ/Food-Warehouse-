package ru.jordan.food_storage.facade.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.facade.product.ProductFacade;
import ru.jordan.food_storage.model.Product;
import ru.jordan.food_storage.service.product.ProductServiceImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // Транзакция только для чтения
public class ProductFacadeImpl implements ProductFacade {

    private final ProductServiceImpl productService;

    @Override
    public Product getProductById(Long id) {
        return productService.getProductById(id);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public Product saveProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public Product updateProduct(ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public List<Product> findProductsByName(String name) {
        return productService.findProductsByName(name);
    }
}
