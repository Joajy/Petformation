package com.Kim.blog.controller.api;

import com.Kim.blog.dto.ResponseDto;
import com.Kim.blog.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmApiController {

    private final AlarmService alarmService;

    @PutMapping("/api/confirm/{reply_id}")
    public ResponseDto<Integer> alarmConfirm(@PathVariable Long reply_id) {
        alarmService.alarmConfirm(reply_id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
