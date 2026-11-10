package com.rafael.ecommerce.catalog.application;

import com.rafael.ecommerce.catalog.api.ProductRequest;
import com.rafael.ecommerce.catalog.api.ProductResponse;
import com.rafael.ecommerce.catalog.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

	public Product toEntity(ProductRequest request) {
		return new Product(request.name(), request.description(), request.price(), request.stockQuantity());
	}

	public ProductResponse toResponse(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStockQuantity(),
				product.getCreatedAt(),
				product.getUpdatedAt()
		);
	}
}
