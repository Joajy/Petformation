package com.Kim.blog.repository;

import com.Kim.blog.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {

    @Modifying
    @Query("update Board b set b.count = b.count + 1 where b.id = :id")
    void updateHit(Long id);

    //LAG로 이전 행의 값 리턴
    @Query(value = "SELECT * FROM board "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LAG(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board) B "
            + "WHERE id = :id)", nativeQuery = true)
    Board findPrevBoard(Long id);

    //LEAD로 다음 행의 값 리턴
    @Query(value = "SELECT * FROM board "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LEAD(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board) B "
            + "WHERE id = :id)", nativeQuery = true)
    Board findNextBoard(Long id);

    @Query(value = "SELECT * FROM board WHERE user_id = :user_id ORDER BY id DESC", nativeQuery = true)
    List<Board> findByUserId(Long user_id);
}
