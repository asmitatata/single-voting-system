package com.hamropatro.service;

import com.hamropatro.dto.votesDto.VotesDto;

import java.util.List;
import java.util.UUID;

public interface VotesDetail {
    void castVote(VotesDto voteDto);

    void updateVote(VotesDto votesDto);

    List<VotesDto> findAll();

    long countByElectionId(UUID electionId);

    long countByCandidateId(UUID electionId, UUID candidateId);
}
