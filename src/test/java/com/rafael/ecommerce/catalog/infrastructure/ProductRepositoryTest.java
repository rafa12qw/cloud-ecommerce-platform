package com.rafael.ecommerce.catalog.infrastructure;

import com.rafael.ecommerce.catalog.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ProductRepositoryTest {

	@Container
	static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void configureDatasource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES::getUsername);
		registry.add("spring.datasource.password", POSTGRES::getPassword);
		registry.add("spring.flyway.enabled", () -> true);
	}

	@Test
	void persistsProduct() {
		Product saved = productRepository.saveAndFlush(
				new Product("Keyboard", "Mechanical keyboard", new BigDecimal("49.99"), 10));

		assertThat(productRepository.findById(saved.getId()))
				.isPresent()
				.get()
				.extracting(Product::getName, Product::getPrice, Product::getStockQuantity)
				.containsExactly("Keyboard", new BigDecimal("49.99"), 10);
	}
}
