package com.Kim.blog.service;

import com.Kim.blog.dto.BoardModalDto;
import com.Kim.blog.dto.BoardWriteDto;
import com.Kim.blog.model.Board;
import com.Kim.blog.model.Reply;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.BoardRepository;
import com.Kim.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void write(BoardWriteDto boardWriteDto, User user) {
        Board board = Board.builder()
                .title(boardWriteDto.getTitle())
                .content(boardWriteDto.getContent())
                .user(user)
                .userNickname(user.getNickname())
                .category(boardWriteDto.getCategory())
                .count(0)
                .build();
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<BoardModalDto> findByUser(Long userId) {

        List<Board> board = boardRepository.findByUserId(userId);
        List<BoardModalDto> boardModalDtoList = new ArrayList<>();

        for (Board value : board) {
            BoardModalDto boardModalDto = BoardModalDto.builder()
                    .id(value.getId())
                    .title(value.getTitle())
                    .createDate(value.getCreateDate())
                    .views(value.getCount())
                    .recommend(value.getRecommendCount())
                    .build();

            boardModalDtoList.add(boardModalDto);
        }
        return boardModalDtoList;
    }

    @Transactional
    public Board detail(Long id, HttpServletRequest request, HttpServletResponse response, Long principalId){
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
                    boardRepository.updateCount(id);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(oldCookie);
                }
            }
            else {
                boardRepository.updateCount(id);
                Cookie newCookie = new Cookie("boardView", "[" + id + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(newCookie);
            }
        }

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("글을 읽어올 수 없습니다.(아이디를 찾을 수 없습니다)"));
        board.getRecommends().forEach((recommend) -> {
            if(recommend.getUser().getId().equals(principalId)){
                board.setRecommendState(true);
            }
        });

        if(board.getSeen() == null){
            board.setSeen("[" + principalId.toString() + "]");
        }
        else{
            board.setSeen(board.getSeen() + "[" + principalId.toString() + "]");
        }

        board.setRecommendCount(board.getRecommends().size());
        board.setPrevBoard(boardRepository.findPrevBoard(id, board.getCategory()));
        board.setNextBoard(boardRepository.findNextBoard(id, board.getCategory()));
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
        board.setCategory(requestBoard.getCategory());
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    @Transactional
    public void writeReply(Long boardId, Reply requestReply, User user){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("댓글 작성 실패: 글을 찾을 수 없습니다."));
        requestReply.setUser(user);
        requestReply.setBoard(board);
        requestReply.setAlarmConfirmState(false);
        replyRepository.save(requestReply);
    }

    @Transactional
    public void deleteReply(Long replyId){
        replyRepository.deleteById(replyId);
    }
}
