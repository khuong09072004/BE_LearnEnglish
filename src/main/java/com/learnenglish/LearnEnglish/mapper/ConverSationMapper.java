package com.learnenglish.LearnEnglish.mapper;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.entity.Conversations;

@Component
public class ConverSationMapper {
    public ConverSationRespone toDTO(Conversations item)
    {
        return new ConverSationRespone(item.getId(), item.getTopic().getId(), item.getTitle(), item.getContext(), item.getRoles(), item.getScript());
    }
}
