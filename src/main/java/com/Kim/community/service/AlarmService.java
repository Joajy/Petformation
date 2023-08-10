package com.Kim.community.service;

import com.Kim.community.model.Reply;
import com.Kim.community.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final ReplyRepository replyRepository;

    @Transactional
    public void alarmConfirm(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("알림 확인 실패: 댓글을 찾을 수 없습니다."));
        reply.setAlarmConfirmState(true);
    }
}
