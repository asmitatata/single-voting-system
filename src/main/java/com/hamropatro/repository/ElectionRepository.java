package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.ElectionEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hamropatro.query.ElectionQuery.*;

@Singleton
public class ElectionRepository {
    private final CqlSession session;

    public ElectionRepository(CqlSession session) {
        this.session = session;
    }

    public void save(ElectionEntity election) {

        var preparedStatement = session.prepare(CREATE_ELECTION);
        var boundStatement = preparedStatement.bind(
                election.getElectionId(),
                election.getElectionName(),
                election.getVotingType(),
                election.getMaxVotes(),
                Instant.ofEpochMilli(election.getStartAt()),
                Instant.ofEpochMilli(election.getEndAt()),
                election.isActive(),
                Instant.ofEpochMilli(election.getCreatedAt()),
                Instant.ofEpochMilli(election.getUpdatedAt()),
                election.getStartTime(),
                election.getEndTime()
        );
        session.execute(boundStatement);
    }

    public Optional<ElectionEntity> findById(UUID electionId) {
        var preparedStatement = session.prepare(FIND_BY_ELECTION_ID_QUERY);
        var boundStatement = preparedStatement.bind(electionId);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.one();
        return Optional.ofNullable(mapElection(row));
    }

    public List<ElectionEntity> findAll() {
        var preparedStatement = session.prepare(FIND_ALL_ELECTION_QUERY);
        var boundStatement = preparedStatement.bind();
        var resultSet = session.execute(boundStatement);
        var rows = resultSet.all();
        return mapElectionList(rows);
    }

    public void update(UUID electionId, ElectionEntity election) {

        var preparedStatement = session.prepare(UPDATE_ELECTION_QUERY);
        var boundStatement = preparedStatement.bind(
                election.getElectionName(),
                Instant.ofEpochMilli(election.getStartAt()),
                Instant.ofEpochMilli(election.getEndAt()),
                election.isActive(),
                election.getStartTime(),
                election.getEndTime(),
                election.getUpdatedAt(),
                electionId
        );
        session.execute(boundStatement);
    }

    public static ElectionEntity mapElection(Row row) {

        if (row == null) {
            return null;
        }

        return ElectionEntity.builder()
                .electionId(row.getUuid("election_id"))
                .electionName(row.getString("election_name"))
                .votingType(row.getString("voting_type"))
                .maxVotes(row.getInt("max_votes"))
                .startAt(row.getInstant("start_at").toEpochMilli())
                .endAt(row.getInstant("end_at").toEpochMilli())
                .isActive(row.getBoolean("is_active"))
                .createdAt(row.getInstant("created_at").toEpochMilli())
                .updatedAt(row.getInstant("updated_at").toEpochMilli())
                .startTime(row.getString("start_time"))
                .endTime(row.getString("end_time"))
                .build();
    }

    public static List<ElectionEntity> mapElectionList(List<Row> rows) {

        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        return rows.stream()
                .map(ElectionRepository::mapElection)
                .toList();
    }

}
