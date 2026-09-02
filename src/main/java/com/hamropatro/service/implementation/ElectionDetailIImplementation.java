package com.hamropatro.service.implementation;

import com.hamropatro.dto.electionDto.ElectionDto;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.mapper.ElectionMapper;
import com.hamropatro.repository.ElectionRepository;
import com.hamropatro.service.ElectionDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class ElectionDetailIImplementation implements ElectionDetail {

    private final ElectionMapper electionMapper;
    private final ElectionRepository electionRepository;


    private String cacheKey(UUID electionId) {
        return "ELECTION:" + electionId;
    }

    public ElectionDetailIImplementation(ElectionMapper electionMapper, ElectionRepository electionRepository) {
        this.electionMapper = electionMapper;
        this.electionRepository = electionRepository;
    }

    @Override
    public void save(ElectionDto electionDto) {
        ElectionEntity entity = electionMapper.fromElectionDtotoElectionEntity(electionDto);
        entity.setElectionId(UUID.randomUUID());
        electionRepository.save(entity);
    }

    @Override
    public Optional<ElectionDto> findById(UUID electionId) {
        var entity = electionRepository.findById(electionId);
        var elections = entity.map(
                electionMapper::fromElectionEntitytoElectionDto);
        return elections;
    }

    @Override
    public List<ElectionDto> findAll() {
        List<ElectionEntity> entity = electionRepository.findAll();
        return electionMapper.fromElectionEntitiesToElectionDto(entity);
    }

    @Override
    public void update(UUID electionId, ElectionDto electionDto) {
        ElectionEntity entity = electionMapper.fromElectionDtotoElectionEntity(electionDto);
        entity.setElectionId(electionId);
        entity.setUpdatedAt(System.currentTimeMillis());
        electionRepository.update(electionId, entity);
    }
}
