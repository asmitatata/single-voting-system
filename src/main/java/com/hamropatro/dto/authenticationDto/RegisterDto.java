package com.hamropatro.dto.authenticationDto;

import com.hamropatro.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterDto(UUID userId,
                          String email,
                          String name,
                          String password,
                          Role role,
                          boolean isActive,
                          long createdAt,
                          long updatedAt) {
}
