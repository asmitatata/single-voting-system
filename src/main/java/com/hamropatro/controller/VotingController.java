package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.CandidateDto.CandidateDto;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.service.VotesDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@GrpcService
@Slf4j
public class VotingController extends VotingServiceGrpc.VotingServiceImplBase {
    private final VotesDetail votesDetail;

    public VotingController(VotesDetail votesDetail) {
        this.votesDetail = votesDetail;
    }

    @Override
    public void castVote(CastVoteRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdminOrVoter();

        var dto = VotesDto.builder()
                .userId(UUID.fromString(request.getUserId()))
                .electionId(UUID.fromString(request.getElectionId()))
                .voteDate(LocalDate.now())
                .candidateId(UUID.fromString(request.getCandidateId()))
                .build();

        votesDetail.castVote(dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Vote saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateVote(UpdateVoteRequest request,StreamObserver<Response> responseStreamObserver){
        AuthContext.requireAdminOrVoter();

        var dto = VotesDto.builder()
                .candidateId(UUID.fromString(request.getOldCandidateId()))
                .candidateId(UUID.fromString(request.getNewCandidateId()))
                .userId(UUID.fromString(request.getUserId()))
                .electionId(UUID.fromString(request.getElectionId()))
                .voteDate(LocalDate.now())
                .build();

        votesDetail.updateVote(dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Vote updated successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getAllVote(Empty request , StreamObserver<GetAllVotesResponse> responseStreamObserver){
        AuthContext.requireAdminOrVoter();
        List<VotesDto> dto = votesDetail.findAll();

        List<Votes> votes = dto.stream()
                .map(VotesMapper::fromVotesDtotoVotes)
                .toList();

        var response = GetAllVotesResponse.newBuilder().addAllVotes(votes).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByElectionId(VotesByElectionIdRequest request, StreamObserver<VoteCountResponse> responseStreamObserver) {
        AuthContext.requireAdmin();

        var electionId = UUID.fromString(request.getElectionId());
        long count = votesDetail.countByElectionId(electionId);

        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByCandidateId(VotesByCandidateIdRequest request, StreamObserver<VoteCountResponse> responseStreamObserver) {
        AuthContext.requireAdmin();

        var candidateId = UUID.fromString(request.getCandidateId());
        var electionId = UUID.fromString(request.getElectionId());

        long count = votesDetail.countByCandidateId(electionId, candidateId);

        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
