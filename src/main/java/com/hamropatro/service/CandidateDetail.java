package com.hamropatro.service;

import com.hamropatro.dto.CandidateDto.CandidateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidateDetail {
    void save(CandidateDto candidateDto);

    Optional<CandidateDto> findById(UUID candidateId);

    List<CandidateDto> findAll();

    List<CandidateDto> findByElectionId(UUID electionId);

    void update(UUID candidateId, CandidateDto candidateDto);

    void delete(UUID candidateId,UUID electionId);

}
