package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Conversations;
import com.learnenglish.LearnEnglish.entity.Grammar;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.exception.AuthorizationException;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ConverSationMapper;
import com.learnenglish.LearnEnglish.mapper.GrammarMapper;
import com.learnenglish.LearnEnglish.mapper.TopicMapper;
import com.learnenglish.LearnEnglish.mapper.VocabMapper;
import com.learnenglish.LearnEnglish.repository.ConversationsRepository;
import com.learnenglish.LearnEnglish.repository.GrammarRepository;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

@Service
public class AdminService {
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
    @Autowired
    GrammarMapper grammarMapper;
    @Autowired
    GrammarRepository grammarRepository;
    @Autowired
    LevelsRepository levelsRepository;
    @Autowired
    TopicsService topicsService;
    @Autowired
    TopicMapper topicMapper;

    @Autowired
    ConversationsRepository conversationsRepository;
    @Autowired
    ConverSationMapper converSationMapper;
    // get list vocabularies
    public List<VocaBularyRespone> getVocabularies(String email, Long topicId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        List<Vocabularies> lst = vocabulariesRepository.findByTopicId(topicId);
        return vocabMapper.toListDTO(lst, user);
    }

    // get by id vocabularies
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

    // get list grammar
    public List<GrammarRespone> getGrammars(String email, Long LevelId) {
        Levels level = levelsRepository.findById(LevelId)
                .orElseThrow(() -> new ValidationException("Level không hợp lệ"));
        List<Grammar> grammars = grammarRepository.findByLevel(level);
        return grammarMapper.toListDTO(grammars);
    }

    // get grammar by id
    public GrammarRespone getGrammarByid(String email, Long id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy grammar"));
        return grammarMapper.toDTO(grammar);
    }

    //get list topic
    public List<TopicsRespone> getTopics (Long levelId)
    {
        Levels level=levelsRepository.findById(levelId)
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        List<Topics> lst=topicsRepository.findByLevel(level);
        return topicMapper.toListDTO(lst);
    }
    //get topic by id
    public TopicsRespone getTopicsById (Long levelId)
    {
        Topics topic=topicsRepository.findById(levelId)
                .orElseThrow(()->new ValidationException("Topic không tồn tại"));
        return topicMapper.toDTO(topic);
    }

    //getlist conversation
    public List<ConverSationRespone> getConversations(Long topicId)
    {
        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
        List<Conversations> conversations=conversationsRepository.findByTopic(topic.getId());
        return converSationMapper.toListDTO(conversations);
    }

}
