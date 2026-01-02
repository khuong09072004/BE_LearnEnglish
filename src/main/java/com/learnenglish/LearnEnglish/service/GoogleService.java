package com.learnenglish.LearnEnglish.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learnenglish.LearnEnglish.dto.responses.LoginResponse;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.util.JwtTokenProvider;

import jakarta.transaction.Transactional;

@Service
public class GoogleService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID ;

    @Transactional
    public LoginResponse  googleLogin(String idTokenString) {
        Map<String, Object> payload = verifyGoogleToken(idTokenString);

        String email = (String) payload.get("email");
        String fullName = (String) payload.get("name");
        String picture = (String) payload.get("picture");
        String googleId = (String) payload.get("sub");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setFullName(fullName);
                    u.setAvatar(picture);
                    u.setGoogleId(googleId);
                    u.setRole(User.Role.USER);
                    u.setStatus(User.Status.ACTIVE);
                    return userRepository.save(u);
                });
        String typeAccount = user.getGoogleId() == null ? "Normal" : "Google";
        boolean hasSelectedLevel = user.getLevel() != null;
        String jwt = jwtTokenProvider.generateToken(user);
        return new LoginResponse(
            jwt,
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getAvatar(),
            typeAccount,
            hasSelectedLevel);
    }

    private Map<String, Object> verifyGoogleToken(String idToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !GOOGLE_CLIENT_ID.equals(response.get("aud"))) {
                throw new ValidationException("Invalid Google token or audience");
            }

            return response;
        } catch (Exception e) {
            throw new ValidationException("Invalid Google token");
        }
    }
}
