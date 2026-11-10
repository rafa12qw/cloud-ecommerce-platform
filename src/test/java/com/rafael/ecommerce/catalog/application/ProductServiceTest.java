package com.rafael.ecommerce.catalog.application;

import com.rafael.ecommerce.catalog.api.ProductRequest;
import com.rafael.ecommerce.catalog.api.ProductResponse;
import com.rafael.ecommerce.catalog.domain.Product;
import com.rafael.ecommerce.catalog.infrastructure.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository, new ProductMapper());
	}

	@Test
	void createsProduct() {
		ProductRequest request = request();
		Product product = new Product(request.name(), request.description(), request.price(), request.stockQuantity());
		when(productRepository.save(any(Product.class))).thenReturn(product);

		ProductResponse response = productService.create(request);

		assertThat(response.name()).isEqualTo("Keyboard");
		assertThat(response.price()).isEqualByComparingTo("49.99");
		verify(productRepository).save(any(Product.class));
	}

	@Test
	void throwsWhenProductDoesNotExist() {
		UUID id = UUID.randomUUID();
		when(productRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.findById(id))
				.isInstanceOf(ProductNotFoundException.class)
				.hasMessage("Product not found: " + id);
	}

	private ProductRequest request() {
		return new ProductRequest("Keyboard", "Mechanical keyboard", new BigDecimal("49.99"), 10);
	}
}
