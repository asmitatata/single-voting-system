package com.hamropatro.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class VotesEntity {
    private UUID userId;
    private UUID electionId;
    private LocalDate voteDate;
    private UUID candidateId;
    private long createdAt;
    private long updatedAt;

    public VotesEntity(UUID userId,
                       UUID electionId,
                       LocalDate voteDate,
                       UUID candidateId,
                       long createdAt,
                       long updatedAt
    ) {
        this.electionId = electionId;
        this.voteDate = voteDate;
        this.userId = userId;
        this.candidateId = candidateId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
