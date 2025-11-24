package com.learnenglish.LearnEnglish.service;


import java.security.MessageDigest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    private String getSHA1(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(file.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi SHA-1", e);
        }
    }

    public String uploadImage(MultipartFile file) {
        try {
            String id = getSHA1(file);

            Map result = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of(
                    "public_id", "learn_english/images/" + id,
                    "resource_type", "image",
                    "overwrite", false
                )
            );

            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload ảnh thất bại", e);
        }
    }

    public String uploadAudio(MultipartFile file) {
        try {
            String id = getSHA1(file);

            Map result = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of(
                    "public_id", "learn_english/audios/" + id,
                    "resource_type", "video",
                    "overwrite", false
                )
            );

            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload audio thất bại", e);
        }
    }
}
