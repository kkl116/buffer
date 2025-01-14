package gubu.buffer.postgres;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.domain.product.ProductService;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;
import gubu.buffer.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class PostgresIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_NAME = "Product 1";
    private static final String PRODUCT_COST_NAME = "paper";
    private static final Double PRODUCT_COST_PRICE = 10.00;

    @Autowired
    private ProductService productService;

    @Test
    void shouldAddProductCostSuccessfully() {
        //Given
        var productId = persistProduct();
        var productCostRequestDto = new ProductCostRequestDto("paper", 20.00);
        //When
        var returnedProductCost = productService.addProductCost(productId, productCostRequestDto);

        //Then
        var fetchedProduct = productRepository.findById(productId);
        var productCost = fetchedProduct.get().getProductCosts().getFirst();
        assertEquals("paper", productCost.getName());
        assertEquals(20.00, productCost.getPrice());

        var fetchedProductCost = productCostRepository.findById(productCost.getId());
        assertEquals(productId, fetchedProductCost.get().getProduct().getId());

        //returnedProductCost should have the generated id
        assertNotNull(returnedProductCost.getId());
    }

    @Test
    void shouldUpdateProductCostSuccessfully() {
        //Given
        var productId = persistProduct();
        var costId = persistProductCost(productId);

        var newProductCostRequestDto = new ProductCostRequestDto("new-paper", 20.00);
        //When
        productService.updateProductCost(costId, newProductCostRequestDto);

        //Then
        var fetchedProductCost = productCostRepository.findById(costId);
        assertEquals("new-paper", fetchedProductCost.get().getName());
        assertEquals(20.00, fetchedProductCost.get().getPrice());

        var fetchedProduct = productRepository.findById(productId);
        assertEquals("new-paper", fetchedProduct.get().getProductCosts().getFirst().getName());
        assertEquals(20.00, fetchedProduct.get().getProductCosts().getFirst().getPrice());
    }

    @Test
    void shouldDeleteProductCostSuccessfully() {
        //Given
        var productId = persistProduct();
        persistProductCost(productId);

        //When
        productService.deleteProductCost(1L);

        //Then
        var fetchedProductCost = productCostRepository.findById(1L);
        assertTrue(fetchedProductCost.isEmpty());

        var fetchedProduct = productRepository.findById(1L);
        assertEquals(0, fetchedProduct.get().getProductCosts().size());
    }

    private Long persistProduct() {
        var product = ProductEntity.builder()
            .name(PRODUCT_NAME)
            .build();

        productRepository.save(product);

        return product.getId();
    }

    private Long persistProductCost(Long productId) {
        var savedProductCostRequestDto = new ProductCostRequestDto(PRODUCT_COST_NAME, PRODUCT_COST_PRICE);
        var productCostEntity = productService.addProductCost(productId, savedProductCostRequestDto);

        return productCostEntity.getId();
    }
}
