package com.Kim.community.service;

import com.Kim.community.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private final RecommendRepository recommendRepository;

    @Transactional
    public void recommend(Long boardId, Long principalId) {
        recommendRepository.recommend(boardId, principalId);
        recommendRepository.ascRecommendCount(boardId);
    }

    @Transactional
    public void cancelRecommend(Long boardId, Long principalId){
        recommendRepository.cancelRecommend(boardId, principalId);
        recommendRepository.descRecommendCount(boardId);
    }
}
