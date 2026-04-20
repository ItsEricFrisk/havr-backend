package com.itsericfrisk.havr.dto;

import com.itsericfrisk.havr.model.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank
        String name,

        @Size(max = 500)
        String description,

        String brand,

        String purchaseUrl,

        @NotNull
        ProductStatus status,

        @NotNull
        Long categoryId
) {
}
