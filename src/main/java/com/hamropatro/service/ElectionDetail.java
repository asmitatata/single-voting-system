package com.hamropatro.service;

import com.hamropatro.dto.electionDto.ElectionDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ElectionDetail {
    void save(ElectionDto electionDto);

    Optional<ElectionDto> findById(UUID electionId);

    List<ElectionDto> findAll();

    void update(UUID electionId, ElectionDto electionDto);
}

