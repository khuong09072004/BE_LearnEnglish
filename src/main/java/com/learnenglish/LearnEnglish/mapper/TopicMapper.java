package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.imp.TopicSummary;

@Component
public class TopicMapper {
   public TopicsRespone toDTO(TopicSummary item) {
        return new TopicsRespone(
            item.getId(),
            item.getName(),
            item.getLevel(),
            item.getTotalVocab().intValue()
        );
    }
    public List<TopicsRespone> toListDTO(List<TopicSummary> lst) {
        List<TopicsRespone> responses = new ArrayList<>();
        for (TopicSummary item : lst) {
            TopicsRespone dto = toDTO(item);
            responses.add(dto);
        }
        return responses;
    }
}
