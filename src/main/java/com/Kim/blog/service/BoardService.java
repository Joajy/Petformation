package com.Kim.blog.service;

import com.Kim.blog.model.Board;
import com.Kim.blog.model.Reply;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.BoardRepository;
import com.Kim.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

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
    public Page<Board> searchResult(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    @Transactional
    public Board detail(Long id, HttpServletRequest request, HttpServletResponse response, Long principal_id){
        if(request != null) {
            Cookie[] cookies = request.getCookies();
            Cookie oldCookie = null;
            if (cookies != null) {
                for (Cookie cookie : cookies){
                    if (cookie.getName().equals("boardView"))
                        oldCookie = cookie;
                }
            }
            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                    boardRepository.updateHit(id);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(oldCookie);
                }
            }
            else {
                boardRepository.updateHit(id);
                Cookie newCookie = new Cookie("boardView", "[" + id + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(newCookie);
            }
        }

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("글을 읽어올 수 없습니다.(아이디를 찾을 수 없습니다)"));
        board.getRecommend().forEach((recommend -> {
            if(recommend.getUser().getId().equals(principal_id)){
                board.setRecommendState(true);
            }
        }));
        board.setRecommendCount(board.getRecommend().size());

        board.setPrevBoard(boardRepository.findPrevBoard(id));
        board.setNextBoard(boardRepository.findNextBoard(id));

        return board;
    }

    @Transactional
    public void delete(Long id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("글을 찾을 수 없습니다.(아이디를 찾을 수 없습니다)"));
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    @Transactional
    public void writeReply(Long boardId, Reply requestReply, User user){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("댓글 작성 실패: 글을 찾을 수 없습니다."));
        requestReply.setUser(user);
        requestReply.setBoard(board);
        replyRepository.save(requestReply);
    }

    @Transactional
    public void deleteReply(Long replyId){
        replyRepository.deleteById(replyId);
    }
}
