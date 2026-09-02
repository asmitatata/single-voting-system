package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ElectionEntity {
    private UUID electionId;

    private String electionName;
    private String votingType;
    private int maxVotes;
    private long startAt;
    private long endAt;
    private boolean isActive;
    private long createdAt;
    private long updatedAt;
    private String startTime;
    private String endTime;

    public ElectionEntity(UUID electionId,
                          String electionName,
                          String votingType,
                          int maxVotes,
                          long startAt,
                          long endAt,
                          boolean isActive,
                          long createdAt,
                          long updatedAt,
                          String startTime,
                          String endTime
    ) {

        this.electionId = electionId;
        this.electionName = electionName;
        this.votingType = votingType;
        this.maxVotes = maxVotes;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
