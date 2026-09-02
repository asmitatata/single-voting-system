package com.hamropatro.query;

public class ElectionQuery {

    public static final String CREATE_ELECTION = "INSERT INTO voting_system.election" +
            "(" +
            "election_id,election_name,voting_type,max_votes,start_at,end_at,is_active,created_at,updated_at,start_time,end_time" +
            ")" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    public static final String FIND_BY_ELECTION_ID_QUERY = "SELECT * FROM voting_system.election WHERE election_id=? ";

    public static final String FIND_ALL_ELECTION_QUERY = "SELECT * FROM voting_system.election";

    public static final String UPDATE_ELECTION_QUERY = "UPDATE voting_system.election SET" +
            " election_name=?, " +
            " start_at=?, " +
            " end_at=?, " +
            " is_active = ?," +
            " start_time = ? ," +
            " end_time = ?," +
            "updated_at = ?" +
            " WHERE election_id = ?";
}
