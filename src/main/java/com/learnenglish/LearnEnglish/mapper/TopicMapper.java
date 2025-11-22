package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Topics;

@Component
public class TopicMapper {
    public TopicsRespone toDTO(Topics item)
    {
        return new TopicsRespone(item.getId(), item.getName(), item.getLevel().getCode());
    }

    public List<TopicsRespone> toListDTO(List<Topics> lst) {
        List<TopicsRespone> responses = new ArrayList<>();
        for (Topics item : lst) {
            TopicsRespone dto = toDTO(item);
            responses.add(dto);
        }
        return responses;
    }
}
