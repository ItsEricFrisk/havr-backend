package com.itsericfrisk.havr.dto;

import com.itsericfrisk.havr.model.Roles;

public record MeResponse(Long id, String name, String email, Roles role) {
}
