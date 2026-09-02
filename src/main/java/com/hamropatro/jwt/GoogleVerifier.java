package com.hamropatro.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Singleton
public class GoogleVerifier {

    @Value("${google.client-id}")
    private String clientId;

    private final GoogleIdTokenVerifier verifier;

    public GoogleVerifier(@Value("${google.client-id}") String clientId) {
        this.clientId = clientId;

        verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(List.of(clientId))
                .build();
    }

    public GoogleIdToken verify(String idToken)
            throws GeneralSecurityException, IOException {

        return verifier.verify(idToken);
    }
}
