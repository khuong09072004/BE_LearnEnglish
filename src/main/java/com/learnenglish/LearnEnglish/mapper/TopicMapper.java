package com.learnenglish.LearnEnglish.mapper;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Topics;

@Component
public class TopicMapper {
    public TopicsRespone toDTO(Topics item)
    {
        return new TopicsRespone(item.getId(), item.getName(), item.getLevel().getCode());
    }
}
