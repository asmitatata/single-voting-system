package com.hamropatro.dto.CandidateDto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record CandidateDto(UUID candidateId,
                           UUID electionId,
                           String candidateName,
                           int candidateAge,
                           String candidateEmail,
                           long createdAt,
                           long updatedAt) implements Serializable {
}
