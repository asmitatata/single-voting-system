package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.userDto.UserDto;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.service.UserDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
public class UserController extends UserServiceGrpc.UserServiceImplBase {
    private final UserDetail userDetail;

    public UserController(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public void getUserByUserId(UserIdRequest request, StreamObserver<GetUserResponse> responseObserver) {
        AuthContext.requireAdmin();
        var dto = userDetail.findById(UUID.fromString(request.getUserId()))
                .orElseThrow(() ->
                        new InvalidCredentialException("User Not Found")
                );

        userDetail.findById(dto.userId());
        var response = GetUserResponse.newBuilder()
                .setUserId(String.valueOf(dto.userId()))
                .setUserEmail(dto.email())
                .setUserEmail(dto.name())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listUser(Empty request, StreamObserver<GetAllUserResponse> responseStreamObserver) {
        AuthContext.requireAdmin();
        List<UserDto> dto = userDetail.findAll();

        List<Users> users = dto.stream()
                .map(AuthUserMapper::fromUserDtoToUsers)
                .toList();

        var response = GetAllUserResponse.newBuilder().addAllUser(users).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
