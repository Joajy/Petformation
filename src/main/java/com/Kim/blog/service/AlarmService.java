package com.Kim.blog.service;

import com.Kim.blog.model.Reply;
import com.Kim.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final ReplyRepository replyRepository;

    @Transactional
    public void alarmConfirm(Long reply_id) {
        Reply reply = replyRepository.findById(reply_id).orElseThrow(() -> new IllegalArgumentException("알람 확인 실패: 댓글을 찾을 수 없습니다."));
        reply.setAlarmConfirmState(true);
    }
}
