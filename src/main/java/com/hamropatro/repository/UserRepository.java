package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.hamropatro.entity.UserEntity;
import com.hamropatro.enums.Role;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hamropatro.query.AuthQuery.FIND_ALL_USER_QUERY;
import static com.hamropatro.query.AuthQuery.FIND_BY_USER_ID_QUERY;

@Singleton
public class UserRepository {
    private final CqlSession session;

    public UserRepository(CqlSession session) {
        this.session = session;
    }

    public Optional<UserEntity> findById(UUID userId) {
        var preparedStatement = session.prepare(FIND_BY_USER_ID_QUERY);
        var boundStatement = preparedStatement.bind(userId);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.one();
        return Optional.ofNullable(mapUserEntity(row));
    }

    public List<UserEntity> findAll() {
        var preparedStatement = session.prepare(FIND_ALL_USER_QUERY);
        var boundStatement = preparedStatement.bind();
        var resultSet = session.execute(boundStatement);
        var rows = resultSet.all();
        return userEntityList(rows);
    }

    public static UserEntity mapUserEntity(Row row) {

        if (row == null) {
            return null;
        }

        return new UserEntity(
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

    public static List<UserEntity> userEntityList(List<Row> rows) {

        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        return rows.stream()
                .map(UserRepository::mapUserEntity)
                .toList();
    }

}
