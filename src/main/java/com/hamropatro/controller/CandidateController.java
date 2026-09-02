package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.CandidateDto.CandidateDto;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.service.CandidateDetail;
import com.hamropatro.validation.Validate;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Named;

import java.util.List;
import java.util.UUID;

@GrpcService
public class CandidateController extends CandidateServiceGrpc.CandidateServiceImplBase {
    private final CandidateDetail candidateDetail;
    private final Validate<CandidateDto> validateCandidate;

    public CandidateController(CandidateDetail candidateDetail,
                               @Named("CandidateValidator") Validate<CandidateDto> validateCandidate) {
        this.candidateDetail = candidateDetail;
        this.validateCandidate = validateCandidate;
    }

    @Override
    public void createCandidate(CreateCandidateRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdmin();
        var dto = CandidateDto.builder()
                .electionId(UUID.fromString(request.getElectionId()))
                .candidateName(request.getCandidateName())
                .candidateAge(request.getCandidateAge())
                .candidateEmail(request.getCandidateEmail())
                .build();

        validateCandidate.validate(dto);
        candidateDetail.save(dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Candidate  " + request.getCandidateName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getCandidateByCandidateId(CandidateIdRequest request, StreamObserver<GetCandidateByCandidateIdResponse> responseObserver) {
        AuthContext.requireAdmin();

        var dto = candidateDetail.findById(UUID.fromString(request.getCandidateId()))
                .orElseThrow(() ->
                        new InvalidCredentialException("Candidate Not Found")
                );

        candidateDetail.findById(dto.candidateId());
        var response = GetCandidateByCandidateIdResponse.newBuilder()
                .setCandidateId(String.valueOf(dto.candidateId()))
                .setElectionId(String.valueOf(dto.electionId()))
                .setCandidateName(dto.candidateName())
                .setCandidateAge(dto.candidateAge())
                .setCandidateEmail(dto.candidateName())
                .setCreatedAt(dto.createdAt())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllCandidate(Empty request, StreamObserver<GetAllCandidateResponse> responseStreamObserver) {
        AuthContext.requireAdmin();
        List<CandidateDto> dto = candidateDetail.findAll();

        List<Candidates> candidates = dto.stream()
                .map(CandidateMapper::fromCandidateDtotoCandidates)
                .toList();

        var response = GetAllCandidateResponse.newBuilder().addAllCandidate(candidates).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getAllCandidateByElectionId(CandidateByElectionIdRequest request, StreamObserver<GetAllCandidateResponse> responseStreamObserver) {
        AuthContext.requireAdminOrVoter();
        List<CandidateDto> dto = candidateDetail.findByElectionId(UUID.fromString(request.getElectionId()));

        List<Candidates> candidates = dto.stream()
                .map(CandidateMapper::fromCandidateDtotoCandidates)
                .toList();

        var response = GetAllCandidateResponse.newBuilder().addAllCandidate(candidates).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateCandidate(UpdateCandidateRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdmin();

        var candidateId = UUID.fromString(request.getCandidateId());

        var dto = CandidateDto.builder()
                .candidateId(UUID.fromString(request.getCandidateId()))
                .electionId(UUID.fromString(request.getElectionId()))
                .candidateName(request.getCandidateName())
                .candidateAge(request.getCandidateAge())
                .candidateEmail(request.getCandidateEmail())
                .build();

        validateCandidate.validate(dto);
        candidateDetail.update(candidateId, dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Candidate  " + request.getCandidateName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


    @Override
    public void deleteCandidate(DeleteCandidateRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdmin();

        var candidateId = UUID.fromString(request.getCandidateId());
        var electionId = UUID.fromString(request.getElectionId());

        candidateDetail.delete(candidateId,electionId);

        var response =
                Response.newBuilder()
                        .setMessage("Candidate deleted successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
