package com.itsericfrisk.havr.service;

import com.itsericfrisk.havr.dto.ProductRequest;
import com.itsericfrisk.havr.dto.ProductResponse;
import com.itsericfrisk.havr.model.Category;
import com.itsericfrisk.havr.model.Product;
import com.itsericfrisk.havr.model.User;
import com.itsericfrisk.havr.repository.CategoryRepository;
import com.itsericfrisk.havr.repository.ProductRepository;
import com.itsericfrisk.havr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Find all products by user
     *
     * @param userId Long
     * @return List of ProductResponse
     */
    public Page<ProductResponse> findAll(Long userId, Pageable pageable) {
        return productRepository.findByUserId(userId, pageable).map(ProductResponse::response);
    }

    /**
     * Find product by its ID.
     * userId is to make sure that no other than the user itself can retrieve the product.
     *
     * @param productId Long
     * @param userId    Long
     * @return ProductResponse
     */
    public ProductResponse findById(Long productId, Long userId) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        return ProductResponse.response(product);
    }

    /**
     * Create a new product
     *
     * @param request ProductRequest
     * @param userId  Long
     * @return ProductResponse
     */
    public ProductResponse create(ProductRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setPurchaseUrl(request.purchaseUrl());
        product.setStatus(request.status());
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        product.setCategory(category);
        product.setUser(user);

        return ProductResponse.response(productRepository.save(product));
    }

    /**
     * Update specific product
     *
     * @param productId Long
     * @param request   ProductRequest
     * @param userId    Long
     * @return ProductResponse
     */
    public ProductResponse update(Long productId, ProductRequest request, Long userId) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setPurchaseUrl(request.purchaseUrl());
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        product.setCategory(category);
        product.setStatus(request.status());

        return ProductResponse.response(productRepository.save(product));
    }

    /**
     * Delete product
     *
     * @param productId Long
     * @param userId    Long
     */
    public void delete(Long productId, Long userId) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        productRepository.delete(product);
    }
}
