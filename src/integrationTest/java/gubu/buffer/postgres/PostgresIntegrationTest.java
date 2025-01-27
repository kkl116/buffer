package gubu.buffer.postgres;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductDimensionRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.product.ProductService;
import gubu.buffer.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class PostgresIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_NAME = "Product 1";
    private static final String PRODUCT_COST_NAME = "paper";
    private static final Double PRODUCT_COST_PRICE = 10.00;
    private static final String PRODUCT_DESCRIPTION = "Product Description";

    @Autowired
    private ProductService productService;

    @Test
    void shouldGetAllProductsSuccessfully() {
        //Given
        var productId1 = persistProduct();
        var productId2 = persistProduct();
        persistProductCost(1L);
        persistProductCost(1L);

        //When
        var allProducts = productService.getAllProducts(List.of());

        //Then
        assertEquals(2, allProducts.size());
        assertEquals(productId1, allProducts.get(0).getId());
        assertEquals(productId2, allProducts.get(1).getId());
    }

    @Test
    void shouldGetAllProductsWithFieldsSuccessfully() {
        //Given
        var productId1 = persistProduct();
        var productId2 = persistProduct();

        //When
        var fields = List.of("id", "name", "description");
        var allProducts = productService.getAllProducts(fields);

        //Then
        assertEquals(2, allProducts.size());
        assertEquals(productId1, allProducts.get(0).getId());
        assertEquals(productId2, allProducts.get(1).getId());
        assertEquals(PRODUCT_NAME, allProducts.get(0).getName());
        assertEquals(PRODUCT_NAME, allProducts.get(1).getName());
        assertNull(allProducts.get(0).getDimensions());
        assertNull(allProducts.get(1).getCosts());
        assertEquals(PRODUCT_DESCRIPTION, allProducts.get(0).getDescription());
        assertEquals(PRODUCT_DESCRIPTION, allProducts.get(1).getDescription());
    }

    @Test
    void shouldGetProductSuccessfully() {
        //Given
        var productId1 = persistProduct();
        var productId2 = persistProduct();
        persistProductCost(productId1);
        persistProductCost(productId1);

        //When
        var fetchedProduct = productService.getProductById(productId1, List.of()).get();

        //Then
        assertEquals(productId1, fetchedProduct.getId());
        assertEquals(PRODUCT_NAME, fetchedProduct.getName());
        assertNotNull(fetchedProduct.getDimensions());
        assertNotNull(fetchedProduct.getCosts());
        assertNotNull(fetchedProduct.getDescription());
    }

    @Test
    void shouldGetProductWithFieldsSuccessfully() {
        //Given
        var productId1 = persistProduct();
        var productId2 = persistProduct();
        persistProductCost(productId1);
        persistProductCost(productId1);

        //When
        var fields = List.of("name", "dimensions", "costs");
        var fetchedProduct = productService.getProductById(productId1, fields).get();

        //Then
        assertEquals(PRODUCT_NAME, fetchedProduct.getName());
        assertNotNull(fetchedProduct.getDimensions());
        assertNotNull(fetchedProduct.getCosts());
    }

    @Test
    void shouldAddProductSuccessfully() {
        //Given
        var productRequestDto = new ProductRequestDto(PRODUCT_NAME, null);
        //When
        var returnedProduct = productService.addProduct(productRequestDto);

        //Then
        var fetchedProduct = productRepository.findById(returnedProduct.getId());
        assertEquals(PRODUCT_NAME, fetchedProduct.get().getName());
        assertNotNull(fetchedProduct.get().getDimensions());

        var fetchedProductDimension = productDimensionRepository.findById(returnedProduct.getId());
        assertTrue(fetchedProductDimension.isPresent());
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        //Given
        var productId = persistProduct();
        var productCostId = persistProductCost(productId);

        //When
        productService.deleteProduct(productId);

        //Then
        var fetchedProduct = productRepository.findById(productId);
        assertTrue(fetchedProduct.isEmpty());

        var fetchedProductCost = productCostRepository.findById(productCostId);
        assertTrue(fetchedProductCost.isEmpty());

        var fetchedProductDimension = productDimensionRepository.findById(productId);
        assertTrue(fetchedProductDimension.isEmpty());
    }

    @Test
    void shouldAddProductCostSuccessfully() {
        //Given
        var productId = persistProduct();
        var productCostRequestDto = new ProductCostRequestDto("paper", 20.00);
        //When
        var returnedProductCost = productService.addProductCost(productId, productCostRequestDto);

        //Then
        var fetchedProduct = productRepository.findById(productId);
        var productCost = fetchedProduct.get().getCosts().getFirst();
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
        assertEquals("new-paper", fetchedProduct.get().getCosts().getFirst().getName());
        assertEquals(20.00, fetchedProduct.get().getCosts().getFirst().getPrice());
    }

    @Test
    void shouldDeleteProductCostSuccessfully() {
        //Given
        var productId = persistProduct();
        var productCostId = persistProductCost(productId);

        //When
        productService.deleteProductCost(productCostId);

        //Then
        var fetchedProductCost = productCostRepository.findById(productCostId);
        assertTrue(fetchedProductCost.isEmpty());

        var fetchedProduct = productRepository.findById(productId);
        assertEquals(0, fetchedProduct.get().getCosts().size());
    }

    @Test
    void shouldUpdateProductDimensionSuccessfully() {
        //Given
        var productId = persistProduct();
        var productDimensionRequestDto = new ProductDimensionRequestDto(1.00, null, null);
        //When
        productService.updateProductDimension(productId, productDimensionRequestDto);

        //Then
        var fetchedProduct = productRepository.findById(productId);
        assertEquals(1.00, fetchedProduct.get().getDimensions().getHeight());
        assertEquals(0.00, fetchedProduct.get().getDimensions().getWidth());
        assertEquals(0.00, fetchedProduct.get().getDimensions().getDepth());

        var fetechedProductDimension = productDimensionRepository.findById(productId);
        assertEquals(1.00, fetechedProductDimension.get().getHeight());
        assertEquals(0.00, fetechedProductDimension.get().getWidth());
        assertEquals(0.00, fetechedProductDimension.get().getDepth());
    }


    private Long persistProduct() {
        var savedProductRequestDto = new ProductRequestDto(PRODUCT_NAME, PRODUCT_DESCRIPTION);
        var product = productService.addProduct(savedProductRequestDto);

        return product.getId();
    }

    private Long persistProductCost(Long productId) {
        var savedProductCostRequestDto = new ProductCostRequestDto(PRODUCT_COST_NAME, PRODUCT_COST_PRICE);
        var productCost = productService.addProductCost(productId, savedProductCostRequestDto);

        return productCost.getId();
    }
}
