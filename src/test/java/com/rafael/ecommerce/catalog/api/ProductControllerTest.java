package com.rafael.ecommerce.catalog.api;

import com.rafael.ecommerce.catalog.application.ProductNotFoundException;
import com.rafael.ecommerce.catalog.application.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

	private final ProductService productService = mock(ProductService.class);
	private final MockMvc mockMvc = MockMvcBuilders
			.standaloneSetup(new ProductController(productService))
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();

	@Test
	void createsProduct() throws Exception {
		UUID id = UUID.randomUUID();
		when(productService.create(any(ProductRequest.class)))
				.thenReturn(response(id));

		mockMvc.perform(post("/api/v1/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Keyboard","description":"Mechanical keyboard","price":49.99,"stockQuantity":10}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/v1/products/" + id))
				.andExpect(jsonPath("$.id").value(id.toString()));
	}

	@Test
	void rejectsInvalidProduct() throws Exception {
		mockMvc.perform(post("/api/v1/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"","description":"","price":-1,"stockQuantity":-2}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Validation failed"));
	}

	@Test
	void returnsNotFound() throws Exception {
		UUID id = UUID.randomUUID();
		when(productService.findById(id)).thenThrow(new ProductNotFoundException(id));

		mockMvc.perform(get("/api/v1/products/" + id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Product not found"));
	}

	@Test
	void updatesProduct() throws Exception {
		UUID id = UUID.randomUUID();
		when(productService.update(eq(id), any(ProductRequest.class))).thenReturn(response(id));

		mockMvc.perform(put("/api/v1/products/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Updated","description":"Updated description","price":59.99,"stockQuantity":5}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Keyboard"));
	}

	@Test
	void deletesProduct() throws Exception {
		UUID id = UUID.randomUUID();
		doNothing().when(productService).delete(id);

		mockMvc.perform(delete("/api/v1/products/" + id))
				.andExpect(status().isNoContent());
	}

	private ProductResponse response(UUID id) {
		return new ProductResponse(id, "Keyboard", "Mechanical keyboard",
				new BigDecimal("49.99"), 10, null, null);
	}
}
