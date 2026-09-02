package com.hamropatro.query;

public class VotesQuery {

    public static final String SAVE_VOTE = "INSERT INTO voting_system.votes " +
            "(" +
            "user_id, election_id,candidate_id,vote_date,created_at,updated_at" +
            ") " +
            "VALUES (?,?,?,?,?,?)";

    public static final String GET_VOTE_COUNT_BY_CANDIDATE_QUERY = "SELECT COUNT(*) FROM voting_system.votes " +
            "where election_id =? AND candidate_id = ?";

    public static final String GET_VOTE_COUNT_BY_ELECTION_QUERY = "SELECT COUNT(*) FROM voting_system.votes " +
            "where election_id =?";

    public static final String GET_All_VOTE_QUERY = "SELECT * FROM voting_system.votes";

    public static final String DELETE_VOTE_QUERY = "DELETE FROM voting_system.votes "+
            "WHERE election_id = ? AND candidate_id = ? AND user_id = ? AND vote_date = ?";
}
