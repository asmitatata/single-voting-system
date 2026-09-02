package com.hamropatro.interceptor;

import com.hamropatro.controller.*;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;

@Singleton
public class GRPCServer<T extends ServerBuilder<T>> implements BeanCreatedEventListener<T> {
    private final ElectionController electionController;
    private final UserController userController;
    private final CandidateController candidateController;
    private final VotingController votingController;
    private final AuthController authController;
    private final JwtInterceptor jwtInterceptor;
    private final ExceptionHandlingInterceptor exceptionHandlingInterceptor;

    public GRPCServer(ElectionController electionController,
                      UserController userController,
                      CandidateController candidateController,
                      VotingController votingController,
                      AuthController authController,
                      JwtInterceptor jwtInterceptor,
                      ExceptionHandlingInterceptor exceptionHandlingInterceptor
    ) {
        this.electionController = electionController;
        this.userController = userController;
        this.candidateController = candidateController;
        this.votingController = votingController;
        this.authController = authController;
        this.jwtInterceptor = jwtInterceptor;
        this.exceptionHandlingInterceptor = exceptionHandlingInterceptor;
    }

    @Override
    public T onCreated(BeanCreatedEvent<T> event) {
        return event.getBean()
                .addService(ServerInterceptors.intercept(
                        electionController,
                        jwtInterceptor,
                        exceptionHandlingInterceptor

                ))
                .addService(ServerInterceptors.intercept(
                        candidateController,
                        jwtInterceptor,
                        exceptionHandlingInterceptor
                ))
                .addService(ServerInterceptors.intercept(
                        userController,
                        jwtInterceptor,
                        exceptionHandlingInterceptor
                ))
                .addService(ServerInterceptors.intercept(
                        votingController,
                        jwtInterceptor,
                        exceptionHandlingInterceptor
                ))
                .addService(ServerInterceptors.intercept(
                        authController,
                        exceptionHandlingInterceptor
                ));
    }
}
