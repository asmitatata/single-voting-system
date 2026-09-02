package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.hamropatro.query.CandidateQuery.DELETE_CANDIDATE_QUERY;
import static com.hamropatro.query.ElectionQuery.FIND_ALL_ELECTION_QUERY;
import static com.hamropatro.query.VotesQuery.*;

@Singleton
public class VoteRepository {
    private final CqlSession session;

    public VoteRepository(CqlSession session) {
        this.session = session;
    }

    public void saveVotes(VotesEntity vote) {
        PreparedStatement preparedStatement = session.prepare(SAVE_VOTE);

        BoundStatement boundStatement = preparedStatement.bind(
                vote.getUserId(),
                vote.getElectionId(),
                vote.getCandidateId(),
                vote.getVoteDate(),
                Instant.ofEpochMilli(vote.getCreatedAt()),
                Instant.ofEpochMilli(vote.getUpdatedAt())
        );

        session.execute(boundStatement);
    }

    public long getAllCandidateVoteCount(UUID electionId, UUID candidateId) {
        PreparedStatement preparedStatement = session.prepare(GET_VOTE_COUNT_BY_CANDIDATE_QUERY);
        var boundStatement = preparedStatement.bind(electionId,candidateId);
        var resultset = session.execute(boundStatement);
        return resultset.one().getLong(0);
    }

    public long getElectionTotalVoteCount(UUID electionId) {
        PreparedStatement preparedStatement = session.prepare(GET_VOTE_COUNT_BY_ELECTION_QUERY);
        var boundStatement = preparedStatement.bind(electionId);
        var resultset = session.execute(boundStatement);
        return resultset.one().getLong(0);
    }

    public List<VotesEntity> findAll() {
        var preparedStatement = session.prepare(GET_All_VOTE_QUERY);
        var boundStatement = preparedStatement.bind();
        var resultSet = session.execute(boundStatement);
        var rows = resultSet.all();
        return mapVotesList(rows);
    }

    public void delete(UUID electionId, UUID candidateId,UUID userId , LocalDate voteDate) {
        PreparedStatement preparedStatement = session.prepare(DELETE_VOTE_QUERY);
        var boundStatement = preparedStatement.bind(electionId,candidateId,userId,voteDate);
        session.execute(boundStatement);
    }

    public void updateVote(
            UUID userId,
            UUID electionId,
            UUID oldCandidateId,
            UUID newCandidateId,
            LocalDate voteDate
    ) {
        long now = System.currentTimeMillis();
        delete(
                electionId,
                oldCandidateId,
                userId,
                voteDate
        );

        VotesEntity newVote = VotesEntity.builder()
                .userId(userId)
                .electionId(electionId)
                .candidateId(newCandidateId)
                .voteDate(voteDate)
                .createdAt(now)
                .updatedAt(now)
                .build();

        saveVotes(newVote);
    }

    public List<VotesEntity> findAllByUser(UUID userId) {
        var preparedStatement = session.prepare(GET_All_VOTE_QUERY);
        var resultSet = session.execute(preparedStatement.bind());

        return resultSet.all().stream()
                .map(VoteRepository::mapVotes)
                .filter(vote -> vote.getUserId().equals(userId))
                .toList();
    }

    public static VotesEntity mapVotes(Row row) {

        if (row == null) {
            return null;
        }

        return VotesEntity.builder()
                .candidateId(row.getUuid("candidate_id"))
                .electionId(row.getUuid("election_id"))
                .userId(row.getUuid("user_id"))
                .voteDate(row.getLocalDate("vote_date"))
                .build();
    }

    public static List<VotesEntity> mapVotesList(List<Row> rows) {

        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        return rows.stream()
                .map(VoteRepository::mapVotes)
                .toList();
    }

}
