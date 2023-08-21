package com.Kim.community.repository;

import com.Kim.community.model.Recommend;
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
    @Query(value = "UPDATE board b SET b.recommendCount = b.recommendCount + 1 WHERE b.id = :board_id", nativeQuery = true)
    void ascRecommendCount(Long board_id);

    @Modifying
    @Query(value = "UPDATE board b SET b.recommendCount = b.recommendCount - 1 WHERE b.id = :board_id", nativeQuery = true)
    void descRecommendCount(Long board_id);
}