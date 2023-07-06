package com.Kim.blog.service;

import com.Kim.blog.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private final RecommendRepository recommendRepository;

    @Transactional
    public void recommend(Long board_id, Long principal_id) {
        recommendRepository.recommend(board_id, principal_id);
        recommendRepository.ascRecommendCount(board_id);
    }

    @Transactional
    public void cancelRecommend(Long board_id, Long principal_id){
        recommendRepository.cancelRecommend(board_id, principal_id);
        recommendRepository.descRecommendCount(board_id);
    }
}
