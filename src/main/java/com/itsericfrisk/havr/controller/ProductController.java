package com.itsericfrisk.havr.controller;

import com.itsericfrisk.havr.dto.ProductRequest;
import com.itsericfrisk.havr.dto.ProductResponse;
import com.itsericfrisk.havr.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();

        return ResponseEntity.ok(service.findAll(userId, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long productId) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();

        return ResponseEntity.ok(service.findById(productId, userId));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();

        return ResponseEntity.ok(service.create(request, userId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(@RequestBody @Valid ProductRequest request, @PathVariable Long productId) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();

        return ResponseEntity.ok(service.update(productId, request, userId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();

        service.delete(productId, userId);

        return ResponseEntity.noContent().build();
    }
}
