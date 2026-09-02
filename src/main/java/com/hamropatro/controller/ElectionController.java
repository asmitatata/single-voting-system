package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.CandidateDto.CandidateDto;
import com.hamropatro.dto.electionDto.ElectionDto;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.mapper.ElectionMapper;
import com.hamropatro.service.CandidateDetail;
import com.hamropatro.service.ElectionDetail;
import com.hamropatro.validation.Validate;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@GrpcService
@Slf4j
public class ElectionController extends ElectionServiceGrpc.ElectionServiceImplBase {
    private final ElectionDetail electionDetail;
    private final CandidateDetail candidateDetail;
    private final Validate<ElectionDto> validateElection;

    public ElectionController(ElectionDetail electionDetail, CandidateDetail candidateDetail,
                              @Named("ElectionValidator") Validate<ElectionDto> validateElection) {
        this.electionDetail = electionDetail;
        this.candidateDetail = candidateDetail;
        this.validateElection = validateElection;

    }

    @Override
    public void createElection(CreateElectionRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdmin();
        var dto = ElectionDto.builder()
                .electionName(request.getElectionName())
                .votingType(request.getVotingType())
                .maxVotes(request.getMaxVotes())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .isActive(request.getIsActive())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        validateElection.validate(dto);
        electionDetail.save(dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Election  " + request.getElectionName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getElectionByElectionId(ElectionIdRequest request, StreamObserver<GetElectionResponse> responseObserver) {
        AuthContext.requireAdmin();
        var dto = electionDetail.findById(UUID.fromString(request.getElectionId()))
                .orElseThrow(() ->
                        new InvalidCredentialException("Election Not Found")
                );

        electionDetail.findById(dto.electionId());
        var response = GetElectionResponse.newBuilder()
                .setElectionId(String.valueOf(dto.electionId()))
                .setElectionName(dto.electionName())
                .setVotingType(dto.votingType())
                .setMaxVotes(dto.maxVotes())
                .setStartAt(dto.startAt())
                .setEndAt(dto.endAt())
                .setCreatedAt(dto.createdAt())
                .setStartTime(dto.startTime())
                .setEndTime(dto.endTime())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllElections(Empty request, StreamObserver<GetAllElectionResponse> responseStreamObserver) {
        AuthContext.requireAdminOrVoter();

        List<ElectionDto> dto = electionDetail.findAll();
        List<Elections> elections = dto.stream()
                .map(election -> {
                    Elections.Builder electionBuilder = ElectionMapper
                            .fromElectionDtotoElections(election)
                            .toBuilder();

                    List<CandidateDto> candidates =
                            candidateDetail.findByElectionId(election.electionId());

                    List<Candidates> candidatesLists = candidates.stream()
                            .map(CandidateMapper::fromCandidateDtotoCandidates)
                            .toList();

                    electionBuilder.addAllCandidate(candidatesLists);

                    return electionBuilder.build();
                })
                .toList();

        var response = GetAllElectionResponse.newBuilder().addAllElections(elections).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateElection(UpdateElectionRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdmin();

        var electionId = UUID.fromString(request.getElectionId());
        var dto = ElectionDto.builder()
                .electionId(UUID.fromString(request.getElectionId()))
                .electionName(request.getElectionName())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .isActive(request.getIsActive())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        validateElection.validateUpdate(dto);
        electionDetail.update(electionId, dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Election  " + request.getElectionName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
