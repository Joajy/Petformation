package com.Kim.community.controller.api;

import java.util.List;

import com.Kim.community.dto.ReplyModalDto;
import com.Kim.community.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReplyApiController {

    private final ReplyService replyService;

    @GetMapping("/api/reply/{user_id}")
    public ResponseEntity<?> findByUser(@PathVariable("user_id") Long id) {

        List<ReplyModalDto> replyModalDto = replyService.findByUser(id);

        return new ResponseEntity<>(replyModalDto, HttpStatus.OK);
    }
}
