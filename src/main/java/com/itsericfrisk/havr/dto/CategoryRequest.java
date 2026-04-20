package com.itsericfrisk.havr.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String name,

        String icon,

        Long parentId
) {
}
