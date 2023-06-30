package com.Kim.blog.service;

import com.Kim.blog.model.Board;
import com.Kim.blog.model.Reply;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.BoardRepository;
import com.Kim.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void write(Board board, User user) {
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board detail(int id){
        return boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글을 읽어올 수 없습니다.(아이디를 찾을 수 없습니다)");
                });
    }

    @Transactional
    public void delete(int id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글을 찾을 수 없습니다.(아이디를 찾을 수 없습니다)");
                });
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    @Transactional
    public void writeReply(int boardId, Reply requestReply, User user){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 작성 실패: 글을 찾을 수 없습니다.");
        });
        requestReply.setUser(user);
        requestReply.setBoard(board);
        replyRepository.save(requestReply);
    }
}
