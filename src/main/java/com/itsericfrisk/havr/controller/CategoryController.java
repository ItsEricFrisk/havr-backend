package com.itsericfrisk.havr.controller;

import com.itsericfrisk.havr.dto.CategoryRequest;
import com.itsericfrisk.havr.dto.CategoryResponse;
import com.itsericfrisk.havr.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllById() {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication()).getPrincipal();

        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication()).getPrincipal();

        return ResponseEntity.ok(service.create(request, userId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(@RequestBody @Valid CategoryRequest request, @PathVariable Long categoryId) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication()).getPrincipal();

        return ResponseEntity.ok(service.update(categoryId, request, userId));

    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication()).getPrincipal();

        service.delete(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}
