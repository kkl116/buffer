package gubu.buffer.postgres;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.domain.product.ProductService;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import gubu.buffer.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class PostgresIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    private static final ProductEntity PRODUCT_1 = ProductEntity.builder()
        .name("Product 1")
        .build();

    @Test
    void shouldAddProductCostSuccessfully() {
        //Given
        productRepository.save(PRODUCT_1);

        var productCostRequestDto = new ProductCostRequestDto("paper", 20.00);
        //When
        var returnedProductCost = productService.addProductCost(1L, productCostRequestDto);

        //Then
        var fetchedProduct = productRepository.findById(1L);
        var productCost = fetchedProduct.get().getProductCosts().getFirst();
        assertEquals("paper", productCost.getName());
        assertEquals(20.00, productCost.getPrice());

        var fetchedProductCost = productCostRepository.findById(productCost.getId());
        assertEquals(1L, fetchedProductCost.get().getProduct().getId());
        assertEquals(1L, fetchedProductCost.get().getId());

        //returnedProductCost should have the generated id
        assertEquals(1L, returnedProductCost.getId());
    }

    @Test
    void shouldUpdateProductCostSuccessfully() {
        //Given
        productRepository.save(PRODUCT_1);
        var savedProductCostRequestDto = new ProductCostRequestDto("old-paper", 10.00);
        productService.addProductCost(1L, savedProductCostRequestDto);

        var newProductCostRequestDto = new ProductCostRequestDto("new-paper", 20.00);
        //When
        productService.updateProductCost(1L, newProductCostRequestDto);

        //Then
        var fetchedProductCost = productCostRepository.findById(1L);
        assertEquals("new-paper", fetchedProductCost.get().getName());
        assertEquals(20.00, fetchedProductCost.get().getPrice());

        var fetchedProduct = productRepository.findById(1L);
        assertEquals("new-paper", fetchedProduct.get().getProductCosts().getFirst().getName());
        assertEquals(20.00, fetchedProduct.get().getProductCosts().getFirst().getPrice());
    }
}
