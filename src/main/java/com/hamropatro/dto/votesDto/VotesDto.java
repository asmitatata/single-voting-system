package com.hamropatro.dto.votesDto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record VotesDto(UUID userId,
                       UUID electionId,
                       LocalDate voteDate,
                       UUID candidateId,
                       long createdAt,
                       long updatedAt) {
}
