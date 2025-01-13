package gubu.buffer;

import com.gubu.buffer.Buffer;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.dimension.ProductDimensionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test") // Like this
@SpringBootTest(classes = Buffer.class)
public abstract class AbstractIntegrationTest {
    private static final String POSTGRES_IMAGE = "postgres:latest";
    private static final String DATABASE_NAME = "buffer_test";
    private static final String USERNAME = "TEST";
    private static final String PASSWORD = "test";

    @Container
    private static PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
        .withDatabaseName(DATABASE_NAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);


    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ProductCostRepository productCostRepository;
    @Autowired
    protected ProductDimensionRepository productDimensionRepository;

    @BeforeEach
    void setup() {
        clearRepositories();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Test
    void contextLoads() {}

    private void clearRepositories() {
        productRepository.deleteAll();
        productCostRepository.deleteAll();
        productDimensionRepository.deleteAll();
    }
}
