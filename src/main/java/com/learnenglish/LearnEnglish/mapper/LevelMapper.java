package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.LevelRespone;
import com.learnenglish.LearnEnglish.entity.Levels;

@Component
public class LevelMapper {
    public LevelRespone toDTO(Levels item) {
        return new LevelRespone(item.getId(), item.getCode(), item.getName(), item.getLevelOrder(), item.getCreated_at());
    }

    public List<LevelRespone> toListDTO(List<Levels> lst)
    {
        List<LevelRespone> respones=new ArrayList<>();
        for (Levels item : lst) {
            LevelRespone dto=toDTO(item);
            respones.add(dto);
        }
        return respones;
    }
}
