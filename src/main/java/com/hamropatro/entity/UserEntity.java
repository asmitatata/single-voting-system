package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.hamropatro.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserEntity {
    private UUID userId;

    private String email;
    private String name;
    private String password;
    private Role role;
    private boolean isActive;
    private long createdAt;
    private long updatedAt;

    public UserEntity(UUID userId,
                      String email,
                      String name,
                      String password,
                      Role role,
                      boolean isActive,
                      long createdAt,
                      long updatedAt
    ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserEntity(UUID userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
