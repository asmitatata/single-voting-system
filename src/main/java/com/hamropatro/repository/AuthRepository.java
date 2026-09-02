package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.dto.userDto.UserDto;
import com.hamropatro.entity.UserEntity;
import com.hamropatro.enums.Role;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.hamropatro.query.AuthQuery.*;

@Singleton
public class AuthRepository {

    private final CqlSession session;

    public AuthRepository(CqlSession session) {
        this.session = session;
    }

    public void register(UserEntity user) {
        var registerPreparedStatement = session.prepare(REGISTER_USER_QUERY);
        var boundStatement = registerPreparedStatement.bind(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getPassword(),
                true,
                Instant.ofEpochMilli(user.getCreatedAt()),
                Instant.ofEpochMilli(user.getUpdatedAt())
        );

        session.execute(boundStatement);
    }

    public Optional<UserDto> findByEmail(String email) {

        var preparedStatement = session.prepare(FIND_BY_EMAIL_QUERY);
        var boundStatement = preparedStatement.bind(email);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.one();

        return Optional.ofNullable(mapUser(row));
    }

    public Optional<UserDto> findById(UUID userId) {

        var preparedStatement = session.prepare(FIND_BY_USER_ID_QUERY);
        var boundStatement = preparedStatement.bind(userId);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.one();

        return Optional.ofNullable(mapUser(row));
    }

    public static UserDto mapUser(Row row) {

        if (row == null) {
            return null;
        }

        return new UserDto(
                row.getUuid("user_id"),
                row.getString("email"),
                row.getString("full_name"),
                row.getString("password"),
                Role.valueOf(row.getString("role").toUpperCase()),
                row.getBoolean("is_active"),
                row.getInstant("created_at").toEpochMilli(),
                row.getInstant("updated_at").toEpochMilli()
        );
    }

}
