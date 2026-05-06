package com.learnenglish.LearnEnglish.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.ContactCreateRequest;
import com.learnenglish.LearnEnglish.dto.responses.ContactResponse;
import com.learnenglish.LearnEnglish.entity.Contact;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.ContactRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactResponse submitContact(String currentUserEmail, ContactCreateRequest req) {
        if (req.getMessage() == null || req.getMessage().isBlank()) {
            throw new ValidationException("Nội dung liên hệ không được để trống");
        }

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản"));

        Contact c = new Contact();
        c.setUser(user);
        c.setFullName(nonBlankOrDefault(req.getFullName(), user.getFullName()));
        c.setEmail(nonBlankOrDefault(req.getEmail(), user.getEmail()));
        c.setPhone(nonBlankOrDefault(req.getPhone(), null));
        c.setMessage(req.getMessage().trim());
        c.setStatus(Contact.ContactStatus.NEW);

        Contact saved = contactRepository.save(c);
        return toResponse(saved);
    }

    public List<ContactResponse> getAllContacts() {
        return contactRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ContactResponse getContactById(Long id) {
        Contact c = contactRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Contact không tồn tại"));
        return toResponse(c);
    }

    public void deleteContact(Long id) {
        Contact c = contactRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Contact không tồn tại"));
        contactRepository.delete(c);
    }

    private ContactResponse toResponse(Contact c) {
        return new ContactResponse(
                c.getId(),
                c.getUser() != null ? c.getUser().getId() : null,
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getMessage(),
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getCreatedAt());
    }

    private String nonBlankOrDefault(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }
}
