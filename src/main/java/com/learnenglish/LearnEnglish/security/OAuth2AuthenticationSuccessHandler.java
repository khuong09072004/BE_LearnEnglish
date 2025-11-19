package com.learnenglish.LearnEnglish.security;

import com.learnenglish.LearnEnglish.util.JwtTokenProvider;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("picture");
        String googleId = oAuth2User.getName(); 

        // 1. TẠO HOẶC CẬP NHẬT USER TRONG DB
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setAvatar(avatarUrl);
            user.setGoogleId(googleId);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            // TẠO USER MỚI
            user = new User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setAvatar(avatarUrl);
            user.setGoogleId(googleId);
            user.setRole(User.Role.USER); 
            user.setStatus(User.Status.ACTIVE);
            userRepository.save(user);
        }

        String jwtToken = jwtTokenProvider.generateToken(user);
        
       String redirectUrl = "http://127.0.0.1:5500/Nhom3/oauth2-success.html?token=" + jwtToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}