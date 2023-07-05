package com.Kim.blog.controller.api;

import com.Kim.blog.config.auth.PrincipalDetail;
import com.Kim.blog.dto.ResponseDto;
import com.Kim.blog.model.Board;
import com.Kim.blog.model.Reply;
import com.Kim.blog.service.BoardService;
import com.Kim.blog.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final RecommendService recommendService;

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.write(board, principal.getUser());

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id) {
        boardService.delete(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
        boardService.update(id, board);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> saveReply(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.writeReply(boardId, reply, principal.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> deleteReply(@PathVariable int replyId) {
        boardService.deleteReply(replyId);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/api/board/{board_id}/recommend")
    public ResponseDto<Integer> recommend(@PathVariable("board_id") Long board_id, @AuthenticationPrincipal PrincipalDetail principal) {
        recommendService.recommend(board_id, (long) principal.getUser().getId());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{board_id}/recommend")
    public ResponseDto<Integer> cancelRecommend(@PathVariable("board_id") Long board_id, @AuthenticationPrincipal PrincipalDetail principal) {
        recommendService.cancelRecommend(board_id, (long) principal.getUser().getId());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
