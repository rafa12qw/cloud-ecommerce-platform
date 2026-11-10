package com.rafael.ecommerce.catalog.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
		@NotBlank @Size(max = 255) String name,
		@NotBlank String description,
		@NotNull @DecimalMin(value = "0.00") BigDecimal price,
		@NotNull @PositiveOrZero Integer stockQuantity
) {
}
