package com.hamropatro.dto.electionDto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ElectionDto(UUID electionId,
                          String electionName,
                          String votingType,
                          int maxVotes,
                          long startAt,
                          long endAt,
                          boolean isActive,
                          long createdAt,
                          long updatedAt,
                          String startTime,
                          String endTime) implements Serializable {
}
