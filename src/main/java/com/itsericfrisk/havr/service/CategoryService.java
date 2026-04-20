package com.itsericfrisk.havr.service;

import com.itsericfrisk.havr.dto.CategoryRequest;
import com.itsericfrisk.havr.dto.CategoryResponse;
import com.itsericfrisk.havr.model.Category;
import com.itsericfrisk.havr.model.User;
import com.itsericfrisk.havr.repository.CategoryRepository;
import com.itsericfrisk.havr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**
     * Find all categories
     *
     * @param userId Long
     * @return List of CategoryResponse
     */
    public List<CategoryResponse> findAll(Long userId) {
        return categoryRepository.findByGlobalTrueOrUserId(userId).stream().map(CategoryResponse::response).toList();
    }

    /**
     * Create a new user specific category.
     *
     * @param request CategoryRequest
     * @param userId  Long
     * @return CategoryResponse
     */
    public CategoryResponse create(CategoryRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = new Category();
        category.setName(request.name());
        category.setIcon(request.icon());
        category.setUser(user);
        category.setGlobal(false);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId()).orElseThrow();
            category.setParent(parent);
        }


        return CategoryResponse.response(categoryRepository.save(category));
    }

    /**
     * Update category
     *
     * @param categoryId Long
     * @param request    CategoryRequest
     * @param userId     Long
     * @return CategoryResponse
     */
    public CategoryResponse update(Long categoryId, CategoryRequest request, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        if (category.isGlobal() || !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId()).orElseThrow();
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        category.setName(request.name());
        category.setIcon(request.icon());

        return CategoryResponse.response(categoryRepository.save(category));
    }

    /**
     * Delete category
     *
     * @param categoryId Long
     * @param userId     Long
     */
    public void delete(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        if (category.isGlobal() || !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        categoryRepository.delete(category);
    }
}
