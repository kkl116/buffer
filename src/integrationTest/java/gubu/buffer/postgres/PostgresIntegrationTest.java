package gubu.buffer.postgres;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.domain.product.ProductService;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import gubu.buffer.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class PostgresIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void shouldAddProductCostSuccessfully() {
        //Given
        var productId = 1L;

        productRepository.save(
            ProductEntity.builder()
            .name("Product 1")
            .build()
        );

        var productCostRequestDto = new ProductCostRequestDto("paper", 20.00);
        //When
        var returnedProductCost = productService.addProductCost(productId, productCostRequestDto);

        //Then
        //product should contain new cost entity
        var fetchedProduct = productRepository.findById(productId);
        assertThat(fetchedProduct.isPresent()).isTrue();
        var productCost = fetchedProduct.get().getProductCosts().getFirst();
        assertEquals("paper", productCost.getName());
        assertEquals(20.00, productCost.getPrice());

        //check cost should contain product entity
        var fetchedProductCost = productCostRepository.findById(productCost.getId());
        assertThat(fetchedProductCost.isPresent()).isTrue();
        assertEquals(productId, fetchedProductCost.get().getProduct().getId());
        assertEquals(1L, fetchedProductCost.get().getId());

        //returnedProductCost should have the generated id
        assertEquals(1L, returnedProductCost.getId());
    }
}
