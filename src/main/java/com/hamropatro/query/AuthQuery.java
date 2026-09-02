package com.hamropatro.query;

public class AuthQuery {

    public static final String REGISTER_USER_QUERY = "INSERT INTO voting_system.users" +
            "(" +
            "user_id,email,full_name,role,password,is_active,created_at,updated_at" +
            ") " +
            "VALUES (?,?,?,?,?,?,?,?)";

    public static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM voting_system.users WHERE email = ?";
    public static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM voting_system.users WHERE user_id = ?";
    public static final String FIND_ALL_USER_QUERY = "SELECT * FROM voting_system.users";

}
