package com.rafael.ecommerce.catalog.application;

import com.rafael.ecommerce.catalog.api.ProductRequest;
import com.rafael.ecommerce.catalog.api.ProductResponse;
import com.rafael.ecommerce.catalog.domain.Product;
import com.rafael.ecommerce.catalog.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	public ProductResponse create(ProductRequest request) {
		Product product = productRepository.save(productMapper.toEntity(request));
		return productMapper.toResponse(product);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> findAll() {
		return productRepository.findAll().stream()
				.map(productMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(UUID id) {
		return productMapper.toResponse(findProduct(id));
	}

	public ProductResponse update(UUID id, ProductRequest request) {
		Product product = findProduct(id);
		product.update(request.name(), request.description(), request.price(), request.stockQuantity());
		return productMapper.toResponse(productRepository.save(product));
	}

	public void delete(UUID id) {
		Product product = findProduct(id);
		productRepository.delete(product);
	}

	private Product findProduct(UUID id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}
}
