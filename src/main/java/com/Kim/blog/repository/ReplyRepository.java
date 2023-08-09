package com.Kim.blog.repository;

import com.Kim.blog.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>{

    @Query(value="select * from reply where board_id in (select id from board where user_id = :user_id) and user_id != :user_id "
            + "order by id desc limit 10", nativeQuery = true)
    List<Reply> findReplyNotification(Long user_id);

    @Query(value = "SELECT * FROM reply WHERE user_id = :user_id ORDER BY id DESC", nativeQuery = true)
    List<Reply> findByUserId(Long user_id);
}