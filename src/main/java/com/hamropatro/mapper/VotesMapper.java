package com.hamropatro.mapper;

import com.hamropatro.Votes;
import com.hamropatro.VotesByElectionId;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;

@Singleton
public class VotesMapper {

    public VotesEntity fromVotesDtoToVotesEntity(VotesDto votesDto) {
        return VotesEntity.builder()
                .userId(votesDto.userId())
                .electionId(votesDto.electionId())
                .voteDate(votesDto.voteDate())
                .candidateId(votesDto.candidateId())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();

    }

    public VotesDto fromVotesEntitytoVotesDto(VotesEntity votesEntity) {
        return VotesDto.builder()
                .userId(votesEntity.getUserId())
                .electionId(votesEntity.getElectionId())
                .voteDate(votesEntity.getVoteDate())
                .candidateId(votesEntity.getCandidateId())
                .createdAt(votesEntity.getCreatedAt())
                .updatedAt(votesEntity.getUpdatedAt())
                .build();
    }

    public static Votes fromVotesDtotoVotes(VotesDto votesDto) {
        return Votes.newBuilder()
                .setElectionId(String.valueOf(votesDto.electionId()))
                .setUserId(String.valueOf(votesDto.userId()))
                .setCandidateId(String.valueOf(votesDto.candidateId()))
                .setCreatedAt(votesDto.createdAt())
                .setUpdatedAt(votesDto.updatedAt())
                .build();
    }

    public static VotesByElectionId fromVotesDtotoVotesByElectionId(VotesDto votesDto) {
        return VotesByElectionId.newBuilder()
                .setElectionId(String.valueOf(votesDto.electionId()))
                .setCandidateId(String.valueOf(votesDto.candidateId()))
                .build();
    }

    public List<VotesDto> fromVotesEntitiesToVotesDto(
            List<VotesEntity> votesEntities) {

        return votesEntities.stream()
                .map(this::fromVotesEntitytoVotesDto)
                .toList();
    }

}
