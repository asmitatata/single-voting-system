package com.hamropatro.mapper;

import com.hamropatro.Elections;
import com.hamropatro.dto.electionDto.ElectionDto;
import com.hamropatro.entity.ElectionEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;

@Singleton
public class ElectionMapper {
    public ElectionEntity fromElectionDtotoElectionEntity(ElectionDto electionDto) {
        return ElectionEntity.builder()
                .electionId(electionDto.electionId())
                .electionName(electionDto.electionName())
                .votingType(electionDto.votingType())
                .maxVotes(electionDto.maxVotes())
                .startAt(electionDto.startAt())
                .endAt(electionDto.endAt())
                .isActive(electionDto.isActive())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .startTime(electionDto.startTime())
                .endTime(electionDto.endTime())
                .build();
    }

    public ElectionDto fromElectionEntitytoElectionDto(ElectionEntity electionEntity) {
        return ElectionDto.builder()
                .electionId(electionEntity.getElectionId())
                .electionName(electionEntity.getElectionName())
                .votingType(electionEntity.getVotingType())
                .maxVotes(electionEntity.getMaxVotes())
                .startAt(electionEntity.getStartAt())
                .endAt(electionEntity.getEndAt())
                .isActive(electionEntity.isActive())
                .createdAt(electionEntity.getCreatedAt())
                .updatedAt(electionEntity.getUpdatedAt())
                .startTime(electionEntity.getStartTime())
                .endTime(electionEntity.getEndTime())
                .build();
    }

    public static Elections fromElectionDtotoElections(ElectionDto electionDto) {
        return Elections.newBuilder()
                .setElectionId(String.valueOf(electionDto.electionId()))
                .setElectionName(electionDto.electionName())
                .setVotingType(electionDto.votingType())
                .setMaxVotes(electionDto.maxVotes())
                .setStartAt(electionDto.startAt())
                .setEndAt(electionDto.endAt())
                .setIsActive(electionDto.isActive())
                .setCreatedAt(electionDto.createdAt())
                .setUpdatedAt(electionDto.updatedAt())
                .setStartTime(electionDto.startTime())
                .setEndTime(electionDto.endTime())
                .build();
    }

    public List<ElectionDto> fromElectionEntitiesToElectionDto(
            List<ElectionEntity> electionEntities) {

        return electionEntities.stream()
                .map(this::fromElectionEntitytoElectionDto)
                .toList();
    }

}
