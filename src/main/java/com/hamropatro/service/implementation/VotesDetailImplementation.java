package com.hamropatro.service.implementation;

import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.entity.VotesEntity;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.repository.VoteRepository;
import com.hamropatro.service.UserDetail;
import com.hamropatro.service.VotesDetail;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class VotesDetailImplementation implements VotesDetail {
    private final VotesMapper votesMapper;
    private final VoteRepository voteRepository;
    private final UserDetail userDetail;

    public VotesDetailImplementation(VotesMapper votesMapper, VoteRepository voteRepository, UserDetail userDetail) {
        this.votesMapper = votesMapper;
        this.voteRepository = voteRepository;
        this.userDetail = userDetail;
    }

    @Override
    public void castVote(VotesDto votesDto) {
        VotesEntity entity = votesMapper.fromVotesDtoToVotesEntity(votesDto);
        voteRepository.saveVotes(entity);
    }

    @Override
    public void updateVote(VotesDto votesDto) {
        UUID newCandidateId = votesDto.candidateId();
        UUID electionId = votesDto.electionId();
        UUID userId = votesDto.userId();

        LocalDate voteDate = votesDto.voteDate();

        List<VotesEntity> votes = voteRepository.findAll();

        VotesEntity existingVote = votes.stream()
                .filter(vote -> vote.getElectionId().equals(electionId))
                .filter(vote -> vote.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Existing vote not found"));

        UUID oldCandidateId = existingVote.getCandidateId();

        voteRepository.updateVote(
                userId,
                electionId,
                oldCandidateId,
                newCandidateId,
                voteDate
        );
    }

    @Override
    public List<VotesDto> findAll() {
        UUID userId = AuthContext.getUserId();
        var entities = voteRepository.findAllByUser(userId);
        return votesMapper.fromVotesEntitiesToVotesDto(entities);
    }


    @Override
    public long countByElectionId(UUID electionId) {
        return voteRepository.getElectionTotalVoteCount(electionId);
    }

    @Override
    public long countByCandidateId(UUID electionId, UUID candidateId) {
        return voteRepository.getAllCandidateVoteCount(electionId, candidateId);
    }
}