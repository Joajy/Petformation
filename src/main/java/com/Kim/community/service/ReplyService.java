package com.Kim.community.service;

import java.util.ArrayList;
import java.util.List;

import com.Kim.community.model.Reply;
import com.Kim.community.repository.ReplyRepository;
import com.Kim.community.dto.ReplyModalDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public List<ReplyModalDto> findByUser(Long userId) {

        List<Reply> replies = replyRepository.findByUserId(userId);
        List<ReplyModalDto> replyModalDtoList = new ArrayList<>();

        for (Reply reply : replies) {
            ReplyModalDto replyModalDto = ReplyModalDto.builder()
                    .id(reply.getId())
                    .content(reply.getContent())
                    .createDate(reply.getCreateDate())
                    .build();
            replyModalDtoList.add(replyModalDto);
        }
        return replyModalDtoList;
    }
}
