package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.LevelRequest;
import com.learnenglish.LearnEnglish.dto.responses.LevelRespone;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.LevelMapper;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;

@Service
public class LevelsService {
    @Autowired
    LevelsRepository levelsRepository;
    @Autowired
    LevelMapper levelmaper;

    public LevelRespone getLevelByid(Long id)
    {
        Levels level=levelsRepository.findById(id)
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        return levelmaper.toDTO(level);
    }

    public List<LevelRespone> getLevels()
    {
        List<Levels> lst=levelsRepository.findAll();
        return levelmaper.toListDTO(lst);
    }

    public LevelRespone createLevel(LevelRequest request)
    {
        Levels level =new Levels();
        level.setCode(request.getCode());
        level.setName(request.getName());
        level.setCreated_at(LocalDateTime.now());
        levelsRepository.save(level);
        return levelmaper.toDTO(level);
    }

    public LevelRespone updateLevelByid(Long id,LevelRequest request)
    {
        Levels level =levelsRepository.findById(id)
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        level.setCode(request.getCode());
        level.setName(request.getName());
        level.setCreated_at(LocalDateTime.now());
        levelsRepository.save(level);
        return levelmaper.toDTO(level);
    }

    public LevelRespone deleteLevelByid(Long id)
    {
         Levels level =levelsRepository.findById(id)
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
         levelsRepository.delete(level);
         return levelmaper.toDTO(level);
    }
}
