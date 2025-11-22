package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.entity.Conversations;

@Component
public class ConverSationMapper {
    public ConverSationRespone toDTO(Conversations item)
    {
        return new ConverSationRespone(item.getId(), item.getTopic().getId(), item.getTitle(), item.getContext(), item.getRoles(), item.getScript());
    }

    public List<ConverSationRespone> toListDTO(List<Conversations> lst)
    {
        List<ConverSationRespone> respones=new ArrayList<>();
        for(Conversations item : lst)
        {
            ConverSationRespone dto=toDTO(item);
            respones.add(dto);
        }
        return respones;
    }
}
