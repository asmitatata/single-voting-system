CREATE KEYSPACE IF NOT EXISTS VOTING_SYSTEM WITH REPLICATION ={
    'class':'SimpleStrategy',
    'replication_factor':1
    }
    AND tablets = {'enabled': false};

USE
    VOTING_SYSTEM;

CREATE TABLE IF NOT EXISTS election
(
    election_id   UUID PRIMARY KEY,
    election_name TEXT,
    voting_type   TEXT,
    max_votes     INT,
    start_at      TIMESTAMP,
    end_at        TIMESTAMP,
    is_active     BOOLEAN,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    start_time    TEXT,
    end_time      TEXT
);


CREATE TABLE IF NOT EXISTS users
(
    user_id    UUID,
    full_name  TEXT,
    email      TEXT,
    role       TEXT,
    password   TEXT,
    is_active  BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY ((user_id), email)
);

CREATE TABLE IF NOT EXISTS candidate
(
    election_id     UUID,
    candidate_id    UUID,
    full_name       TEXT,
    candidate_email TEXT,
    candidate_age   INT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    PRIMARY KEY (election_id, candidate_id)
);


CREATE TABLE votes
(
    user_id      UUID,
    election_id  UUID,
    vote_date    DATE,
    candidate_id UUID,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,

    PRIMARY KEY ((election_id), candidate_id, user_id, vote_date)
);


