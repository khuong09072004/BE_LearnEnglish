package com.learnenglish.LearnEnglish.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.learnenglish.LearnEnglish.dto.responses.UserResponse;
import com.learnenglish.LearnEnglish.entity.User;

@Component
public class UserMapper {

    public UserResponse toDTO(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .dateOfBirth(user.getDateOfBirth())
                .levelCode(user.getLevel() != null ? user.getLevel().getCode() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<UserResponse> toListDTO(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}