package com.Kim.blog.repository;

import com.Kim.blog.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, Long>{

    @Modifying
    @Query(value = "INSERT INTO recommend(board_id, user_id) VALUES(:board_id, :principal_id)", nativeQuery = true)
    int recommend(Long board_id, Long principal_id);

    @Modifying
    @Query(value = "DELETE FROM recommend WHERE board_id = :board_id AND user_id = :principal_id", nativeQuery = true)
    int cancelRecommend(Long board_id, Long principal_id);

    @Modifying
    @Query("update Board b set b.recommendCount = b.recommendCount + 1 where b.id = :board_id")
    void ascRecommendCount(Long board_id);

    @Modifying
    @Query("update Board b set b.recommendCount = b.recommendCount - 1 where b.id = :board_id")
    void descRecommendCount(Long board_id);
}