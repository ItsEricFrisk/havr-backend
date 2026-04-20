package com.itsericfrisk.havr.dto;

import com.itsericfrisk.havr.model.Product;
import com.itsericfrisk.havr.model.ProductStatus;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String brand,
        String purchaseUrl,
        ProductStatus status,
        Long categoryId,
        String categoryName
) {
    public static ProductResponse response(Product request) {
        return new ProductResponse(
                request.getId(),
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                request.getPurchaseUrl(),
                request.getStatus(),
                request.getCategory().getId(),
                request.getCategory().getName()
        );
    }
}
