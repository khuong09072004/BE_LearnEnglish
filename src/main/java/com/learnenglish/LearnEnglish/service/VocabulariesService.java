package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.requests.VocabularyRequest;
import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.exception.AuthorizationException;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.VocabMapper;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

import jakarta.transaction.Transactional;

@Service
public class VocabulariesService {
        @Autowired
        VocabulariesRepository vocabulariesRepository;
        @Autowired
        UserRepository userRepository;
        @Autowired
        TopicsRepository topicsRepository;
        @Autowired
        UserVocabProgressRepository userVocabProgressRepository;
        @Autowired
        private VocabMapper vocabMapper;
        @Autowired
        ImgBBService imgBBService;

        // get list
        public List<VocaBularyRespone> getVocabularies(String email, Long topicId) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

                Topics topic = topicsRepository.findById(topicId)
                                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

                if (!topic.getLevel().getCode().equals(user.getLevel().getCode())) {
                        throw new AuthorizationException("Topic này không phù hợp với trình độ của bạn");
                }

                List<Vocabularies> lst = vocabulariesRepository.findByTopicId(topicId);
                return vocabMapper.toListDTO(lst, user);
        }

        // get by id
        public VocaBularyRespone getVocabulariesById(String email, Long id) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
                Vocabularies voca = vocabulariesRepository.findById(id)
                                .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại"));
                boolean is_learned = userVocabProgressRepository
                                .findByUserIdAndVocabularyId(user.getId(), voca.getId())
                                .map(User_vocab_progress::isLearned)
                                .orElse(false);
                return vocabMapper.toDTO(voca, is_learned);
        }

        // create
        @Transactional
        public VocaBularyRespone createVocabularyByAdmin( VocabularyRequest requests,
                        MultipartFile imageFile) {
                Topics topic = topicsRepository.findById(requests.getTopicId())
                                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));
                Vocabularies vocab = new Vocabularies();
                vocab.setTopic(topic);
                vocab.setDescription(requests.getDescription());
                vocab.setWord(requests.getWord());
                vocab.setMeaning(requests.getMeaning());
                vocab.setPhonetic(requests.getPhonetic());
                String imageUrl = null;
                if (imageFile != null && !imageFile.isEmpty()) {
                        imageUrl = imgBBService.checkAndUploadImage(imageFile);
                }
                vocab.setImage_url(imageUrl);
                vocab.setImage_url(imageUrl);
                vocabulariesRepository.save(vocab);
                return vocabMapper.toDTO(vocab, false);
        }

        // update
        @Transactional
        public VocaBularyRespone updateVocabularyByAdmin( Long vocabId, VocabularyRequest requests,
                        MultipartFile imageFile) {
                Topics topic = topicsRepository.findById(requests.getTopicId())
                                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));
                Vocabularies vocab = vocabulariesRepository.findById(vocabId)
                                .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại"));
                vocab.setTopic(topic);
                vocab.setDescription(requests.getDescription());
                vocab.setWord(requests.getWord());
                vocab.setMeaning(requests.getMeaning());
                vocab.setPhonetic(requests.getPhonetic());
                if (imageFile != null && !imageFile.isEmpty()) {
                        String imageUrl = imgBBService.checkAndUploadImage(imageFile);
                        vocab.setImage_url(imageUrl);
                }
                vocabulariesRepository.save(vocab);
                return vocabMapper.toDTO(vocab, false);
        }

        @Transactional
        public Object deleteVocabularyByAdmin( Long vocabId) {
                Vocabularies vocab = vocabulariesRepository.findById(vocabId)
                                .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại"));
                vocabulariesRepository.delete(vocab);
                return vocabMapper.toDTO(vocab, false);
        }

}
