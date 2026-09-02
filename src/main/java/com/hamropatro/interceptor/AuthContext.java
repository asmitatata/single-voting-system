package com.hamropatro.interceptor;

import com.hamropatro.entity.UserEntity;
import com.hamropatro.enums.Role;
import com.hamropatro.exceptions.PermissionDeniedException;
import com.hamropatro.exceptions.UnauthenticatedException;
import io.grpc.Context;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class AuthContext {
    public static final Context.Key<UserEntity> USER =
            Context.key("authenticated-user");

    public static UserEntity getUser() {
        UserEntity user = USER.get();

        if (user == null) {
            throw new IllegalStateException("No authenticated user");
        }
        return user;
    }

    public static UUID getUserId() {
        var user = USER.get();

        if (user == null) {
            throw new UnauthenticatedException("User is not authenticated");
        }

        return user.getUserId();
    }

    public static Role getRole() {

        UserEntity user = getUser();

        return user.getRole();
    }

    public static void requireAdmin() {
        if (!Role.ADMIN.equals(getRole())) {
            throw new PermissionDeniedException("Admin permission required");
        }
    }

    public static void requireAdminOrVoter() {

        Role role = getRole();

        if (!Role.ADMIN.equals(role) && !Role.VOTER.equals(role)) {
            throw new PermissionDeniedException("Admin or Voter permission required");
        }
    }
}

