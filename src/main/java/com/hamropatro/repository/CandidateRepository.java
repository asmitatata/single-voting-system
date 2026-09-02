package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.CandidateEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hamropatro.query.CandidateQuery.*;

@Singleton
public class CandidateRepository {
    private final CqlSession session;

    public CandidateRepository(CqlSession session) {
        this.session = session;
    }

    public void save(CandidateEntity candidate) {

        var preparedStatement=session.prepare(CREATE_CANDIDATE);

        var boundStatement=preparedStatement.bind(
                candidate.getCandidateId(),
                candidate.getElectionId(),
                candidate.getCandidateName(),
                candidate.getCandidateEmail(),
                candidate.getCandidateAge(),
                Instant.ofEpochMilli(candidate.getCreatedAt()),
                Instant.ofEpochMilli(candidate.getUpdatedAt())
        );

        session.execute(boundStatement);
    }

    public Optional<CandidateEntity> findById(UUID candidateId) {

        var preparedStatement = session.prepare(FIND_BY_CANDIDATE_ID_QUERY);
        var boundStatement = preparedStatement.bind(candidateId);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.one();

        return Optional.ofNullable(mapCandidate(row));
    }

    public List<CandidateEntity> findAll() {

        var preparedStatement = session.prepare(FIND_ALL_CANDIDATE_QUERY);
        var boundStatement = preparedStatement.bind();
        var resultSet = session.execute(boundStatement);
        var row = resultSet.all();

        return mapCandidateLIst(row);
    }

    public List<CandidateEntity> findByElectionId(UUID electionId) {

        var preparedStatement = session.prepare(FIND_CANDIDATES_BY_ELECTION_ID_QUERY);
        var boundStatement = preparedStatement.bind(electionId);
        var resultSet = session.execute(boundStatement);
        var row = resultSet.all();

        return mapCandidateLIst(row);
    }

    public void update(UUID candidateId, CandidateEntity candidate) {

        var preparedStatement=session.prepare(UPDATE_CANDIDATE_QUERY);
        var boundStatement=preparedStatement.bind( candidate.getCandidateName(),
                candidate.getCandidateEmail(),
                candidate.getCandidateAge(),
                Instant.ofEpochMilli(candidate.getUpdatedAt()),
                candidateId,
                candidate.getElectionId()
        );
        session.execute(boundStatement);
    }

    public void delete(UUID candidateId, UUID electionId) {
        var preparedStatement = session.prepare(DELETE_CANDIDATE_QUERY);
        var boundStatement = preparedStatement.bind(candidateId,electionId);
        session.execute(boundStatement);
    }


    private static CandidateEntity mapCandidate(Row row) {

        if (row == null) {
            return null;
        }

        return CandidateEntity.builder()
                .candidateId(row.getUuid("candidate_id"))
                .electionId(row.getUuid("election_id"))
                .candidateName(row.getString("full_name"))
                .candidateEmail(row.getString("candidate_email"))
                .candidateAge(row.getInt("candidate_age"))
                .createdAt(row.getInstant("created_at").toEpochMilli())
                .updatedAt(row.getInstant("updated_at").toEpochMilli())
                .build();
    }

    private List<CandidateEntity> mapCandidateLIst(List<Row> rows) {

        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        return rows.stream()
                .map(CandidateRepository::mapCandidate)
                .toList();
    }

}
