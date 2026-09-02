package com.hamropatro.validation;

import com.hamropatro.dto.CandidateDto.CandidateDto;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named("CandidateValidator")
class CandidateValidator implements Validate<CandidateDto> {

    @Override
    public void validate(CandidateDto candidateDto) {
        if (candidateDto.candidateName() == null || candidateDto.candidateName().isBlank()) {
            throw new IllegalArgumentException("Candidate name is required");
        }
        if (candidateDto.electionId() == null) {
            throw new IllegalArgumentException("Election Id is required");
        }
        if (candidateDto.candidateEmail() == null) {
            throw new IllegalArgumentException("Candidate email is required");
        }
    }

    @Override
    public void validateUpdate(CandidateDto request) {

    }
}
