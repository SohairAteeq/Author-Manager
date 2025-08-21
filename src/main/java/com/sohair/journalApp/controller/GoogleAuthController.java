package com.sohair.journalApp.controller;

import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.UserRepository;
import com.sohair.journalApp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    // 🔑 Must match the Google Cloud Console redirect URI exactly
    private static final String REDIRECT_URI = "http://localhost:8080/auth/google/callback";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Step 1: User comes back here with "code" after Google login
     */
    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        try {
            log.info("Received authorization code: {}", code);

            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", REDIRECT_URI);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            try {
                ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);

                log.info("Token response status: {}", tokenResponse.getStatusCode());

                if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("error", "Failed to get token: " + tokenResponse.getBody()));
                }

                String idToken = (String) tokenResponse.getBody().get("id_token");
                if (idToken == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("error", "No id_token in token response"));
                }

                // Get user info from Google
                String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
                ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

                log.info("User info response status: {}", userInfoResponse.getStatusCode());

                if (userInfoResponse.getStatusCode() == HttpStatus.OK && userInfoResponse.getBody() != null) {
                    Map<String, Object> userInfo = userInfoResponse.getBody();
                    String email = (String) userInfo.get("email");

                    if (email == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Collections.singletonMap("error", "No email found in user info"));
                    }

                    // Find or create worker - FIXED: Set both password and hashedPassword
                    User user = userRepository.findByEmail(email).orElseGet(() -> {
                        String randomPassword = UUID.randomUUID().toString();
                        String encodedPassword = passwordEncoder.encode(randomPassword);

                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setUserName(email);
                        newUser.setPassword(encodedPassword);
                        newUser.setRoles(List.of("USER"));
                        return userRepository.save(newUser);
                    });

                    String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getRoles().get(0));

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtToken);
                    response.put("user", Collections.singletonMap("email", email));

                    return ResponseEntity.ok(response);
                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Failed to fetch user info"));

            } catch (HttpClientErrorException e) {
                log.error("HTTP Error during token exchange: {}", e.getStatusCode());
                log.error("Error response: {}", e.getResponseBodyAsString());

                // More specific error handling for Google OAuth2
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("error",
                                    "Google authentication failed. Please check your client ID and secret."));
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Token exchange failed: " + e.getMessage()));
            }

        } catch (Exception e) {
            log.error("Exception occurred while handling Google Callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Internal server error"));
        }
    }
}
