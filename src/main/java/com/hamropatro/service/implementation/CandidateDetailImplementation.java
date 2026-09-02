package com.hamropatro.service.implementation;

import com.hamropatro.dto.CandidateDto.CandidateDto;
import com.hamropatro.entity.CandidateEntity;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.repository.CandidateRepository;
import com.hamropatro.service.CandidateDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class CandidateDetailImplementation implements CandidateDetail {
    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;

    public CandidateDetailImplementation(CandidateMapper candidateMapper,
                                         CandidateRepository candidateRepository
    ) {
        this.candidateMapper = candidateMapper;
        this.candidateRepository = candidateRepository;
    }

    private String candidateCacheKey(UUID electionId) {
        return "CANDIDATES:" + electionId;
    }

    @Override
    public void save(CandidateDto candidateDto) {
        CandidateEntity entity = candidateMapper.fromCandidateDtotoCandidateEntity(candidateDto);
        entity.setCandidateId(UUID.randomUUID());
        candidateRepository.save(entity);
    }

    @Override
    public Optional<CandidateDto> findById(UUID candidateId) {
        var entity = candidateRepository.findById(candidateId);
        var candidates = entity.map(candidateMapper::fromCandidateEntitytoCandidateDto);
        return candidates;
    }

    @Override
    public List<CandidateDto> findAll() {
        List<CandidateEntity> entity = candidateRepository.findAll();
        return (candidateMapper.fromCandidateEntitiesToCandidateDto(entity));
    }

    @Override
    public List<CandidateDto> findByElectionId(UUID electionId) {
        List<CandidateEntity> entity = candidateRepository.findByElectionId(electionId);
        List<CandidateDto> candidates = candidateMapper.fromCandidateEntitiesToCandidateDto(entity);
        return candidates;

    }

    @Override
    public void update(UUID candidateId, CandidateDto candidateDto) {
        CandidateEntity entity = candidateMapper.fromCandidateDtotoCandidateEntity(candidateDto);
        entity.setCandidateId(candidateId);
        entity.setUpdatedAt(System.currentTimeMillis());
        candidateRepository.update(candidateId, entity);
    }

    @Override
    public void delete(UUID candidateId, UUID electionId) {
        candidateRepository.delete(candidateId, electionId);
    }

}
