package com.hamropatro.validation;

import com.hamropatro.dto.electionDto.ElectionDto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UpdateElectionValidator implements Validate<ElectionDto> {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void validate(ElectionDto request) {

    }

    @Override
    public void validateUpdate(ElectionDto electionDto) {
        if (electionDto.electionName() == null || electionDto.electionName().isBlank()) {
            throw new IllegalArgumentException("Election name is required");
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
}

