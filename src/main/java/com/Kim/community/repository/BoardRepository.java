package com.Kim.community.repository;

import com.Kim.community.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board>{

    //SELECT 제외하고는 무조건 Modifying을 강제함
    @Modifying
    @Query(value = "UPDATE board b set b.count = b.count + 1 where b.id = :id", nativeQuery = true)
    void updateCount(Long id);

    @Query(value = "SELECT * FROM board "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LAG(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board WHERE category = :category) B "
            + "WHERE id = :id)", nativeQuery = true)
    Board findPrevBoard(Long id, String category);

    @Query(value = "SELECT * FROM board "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LEAD(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board WHERE category = :category) B "
            + "WHERE id = :id)", nativeQuery = true)
    Board findNextBoard(Long id, String category);

    @Query(value = "SELECT * FROM board WHERE user_id = :user_id ORDER BY id DESC", nativeQuery = true)
    List<Board> findByUserId(Long user_id);

    @Query(value = "SELECT count(*) FROM board WHERE DATE(createDate) = DATE(now())", nativeQuery = true)
    int countTodayBoard();

    @Query(value = "SELECT count(*) FROM board WHERE DATE(createDate) = DATE(date_add(now(), INTERVAL - 1 DATE))", nativeQuery = true)
    int countYesterdayBoard();

    @Query(value = "SELECT count(*) FROM board", nativeQuery = true)
    int countTotalBoard();

    @Query(value = "SELECT count(*) FROM board WHERE category = :category", nativeQuery = true)
    int countByCategory(String category);
}