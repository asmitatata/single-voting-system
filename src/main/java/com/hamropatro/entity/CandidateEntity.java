package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CandidateEntity {
    private UUID candidateId;
    private UUID electionId;
    private String candidateName;
    private int candidateAge;
    private String candidateEmail;
    private long createdAt;
    private long updatedAt;


    public CandidateEntity(UUID candidateId,
                           UUID electionId,
                           String candidateName,
                           int candidateAge,
                           String candidateEmail,
                           long createdAt,
                           long updatedAt
    ) {

        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.candidateAge = candidateAge;
        this.candidateEmail = candidateEmail;
        this.electionId = electionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
