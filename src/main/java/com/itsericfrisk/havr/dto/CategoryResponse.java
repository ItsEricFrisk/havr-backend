package com.itsericfrisk.havr.dto;

import com.itsericfrisk.havr.model.Category;

public record CategoryResponse(
        Long id,

        String name,

        String icon
) {
    public static CategoryResponse response(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIcon()
        );
    }
}
