package com.Kim.blog.controller;

import com.Kim.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"", "/"})
    public String index(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boards", boardService.boardList(pageable));
        return "index";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("board", boardService.detail(id,request,response));
        System.out.println("Let's start!");
        boardService.detail(id,request,response);
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "/board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("board", boardService.detail(id, request, response));
        return "/board/updateForm";
    }
}