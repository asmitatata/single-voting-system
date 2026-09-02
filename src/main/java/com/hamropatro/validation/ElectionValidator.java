package com.hamropatro.validation;

import com.hamropatro.dto.electionDto.ElectionDto;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Singleton
@Named("ElectionValidator")
class ElectionValidator implements Validate<ElectionDto> {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void validate(ElectionDto electionDto) {
        if (electionDto.electionName() == null || electionDto.electionName().isBlank()) {
            throw new IllegalArgumentException("Election name is required");
        }

        if (electionDto.votingType() == null || electionDto.votingType().isBlank()) {
            throw new IllegalArgumentException("Voting type is required");
        }

        if (electionDto.maxVotes() <= 0) {
            throw new IllegalArgumentException("Max votes must be greater than 0");
        }

        if (electionDto.startAt() >= electionDto.endAt()) {
            throw new IllegalArgumentException(
                    "Start date must be before end date"
            );
        }

        if (electionDto.startTime() == null ||
                electionDto.startTime().isBlank()) {
            throw new IllegalArgumentException(
                    "Voting window start time is required"
            );
        }

        if (electionDto.endTime() == null ||
                electionDto.endTime().isBlank()) {
            throw new IllegalArgumentException(
                    "Voting window end time is required"
            );
        }

        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(
                    electionDto.startTime(),
                    TIME_FORMAT
            );
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Start time must be in HH:mm format"
            );
        }

        try {
            endTime = LocalTime.parse(
                    electionDto.endTime(),
                    TIME_FORMAT
            );
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "End time must be in HH:mm format"
            );
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Voting window start time must be before end time"
            );
        }
    }

    @Override
    public void validateUpdate(ElectionDto request) {

    }
}
