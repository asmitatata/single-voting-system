package com.hamropatro.interceptor;

import com.hamropatro.exceptions.*;
import io.grpc.*;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

@Slf4j
@Singleton
public class ExceptionHandlingInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (ElectionNotFoundException | CandidateNotFoundException |
                         VotesNotFoundException | ElectionNotActiveException e) {
                    log.warn("Exception occurred:", e);
                    call.close(Status.NOT_FOUND.withDescription(e.getMessage()), new Metadata());

                } catch (IllegalArgumentException | NullPointerException | InvalidCredentialException exception) {
                    log.warn("Exception occurred:", exception);
                    call.close(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()), new Metadata());
                } catch (Exception e) {
                    log.warn("Exception occurred:", e);
                    call.close(Status.INTERNAL.withDescription("Internal Server Error"), new Metadata());
                }
            }
        };
    }
}
