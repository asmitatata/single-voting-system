package com.hamropatro.mapper;

import com.hamropatro.Candidates;
import com.hamropatro.dto.CandidateDto.CandidateDto;
import com.hamropatro.entity.CandidateEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;

@Singleton
public class CandidateMapper {
    public CandidateEntity fromCandidateDtotoCandidateEntity(CandidateDto candidateDto) {
        return CandidateEntity.builder()
                .candidateId(candidateDto.candidateId())
                .electionId(candidateDto.electionId())
                .candidateEmail(candidateDto.candidateEmail())
                .candidateName(candidateDto.candidateName())
                .candidateAge(candidateDto.candidateAge())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();
    }

    public CandidateDto fromCandidateEntitytoCandidateDto(CandidateEntity candidateEntity) {
        return CandidateDto.builder()
                .candidateId(candidateEntity.getCandidateId())
                .electionId(candidateEntity.getElectionId())
                .candidateName(candidateEntity.getCandidateName())
                .candidateAge(candidateEntity.getCandidateAge())
                .candidateEmail(candidateEntity.getCandidateEmail())
                .createdAt(candidateEntity.getCreatedAt())
                .updatedAt(candidateEntity.getUpdatedAt())
                .build();
    }

    public static Candidates fromCandidateDtotoCandidates(CandidateDto candidateDto) {
        return Candidates.newBuilder()
                .setCandidateId(String.valueOf(candidateDto.candidateId()))
                .setCandidateName(candidateDto.candidateName())
                .setCandidateAge(candidateDto.candidateAge())
                .setCandidateEmail(candidateDto.candidateEmail())
                .setElectionId(String.valueOf(candidateDto.electionId()))
                .setCreatedAt(candidateDto.createdAt())
                .setUpdatedAt(candidateDto.updatedAt())
                .build();
    }

    public List<CandidateDto> fromCandidateEntitiesToCandidateDto(
            List<CandidateEntity> candidateEntities) {

        return candidateEntities.stream()
                .map(this::fromCandidateEntitytoCandidateDto)
                .toList();
    }

}
