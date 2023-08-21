package com.Kim.community.repository;

import com.Kim.community.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>{

    @Query(value="SELECT * FROM reply WHERE board_id IN (SELECT id FROM board WHERE user_id = :user_id) AND user_id != :user_id "
            + "ORDER BY id DESC limit 10", nativeQuery = true)
    List<Reply> findReplyNotification(Long user_id);

    @Query(value = "SELECT * FROM reply WHERE user_id = :user_id ORDER BY id DESC", nativeQuery = true)
    List<Reply> findByUserId(Long user_id);
}