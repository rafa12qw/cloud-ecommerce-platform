package com.rafael.ecommerce.catalog.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
		UUID id,
		String name,
		String description,
		BigDecimal price,
		Integer stockQuantity,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {
}
