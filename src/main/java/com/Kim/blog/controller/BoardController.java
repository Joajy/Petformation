package com.Kim.blog.controller;

import com.Kim.blog.config.auth.PrincipalDetail;
import com.Kim.blog.model.Board;
import com.Kim.blog.repository.BoardRepository;
import com.Kim.blog.service.BoardService;
import com.Kim.blog.specification.BoardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("/board")
    public String index(Model model,
                        @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "category", defaultValue = "none") String category,
                        @RequestParam(value = "searchType", defaultValue = "") String searchType,
                        @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {

        Specification<Board> spec = (root, query, criteriaBuilder) -> null;

        if (!category.isEmpty()) {
            //추천 수가 20을 넘을 경우 인기 게시판으로 이동
            if (category.equals("popular")) {
                spec = spec.and(BoardSpecification.recGreaterThan(20));
            } else {
                spec = spec.and(BoardSpecification.equalCategory(category));
            }
        }
        if (!searchType.isEmpty()) {
            if (searchType.equals("title")) {
                spec = spec.and(BoardSpecification.searchTypeTitle(searchKeyword));
            } else {
                spec = spec.and(BoardSpecification.searchTypeWriter(searchKeyword));
            }
        }
        model.addAttribute("board", boardRepository.findAll(spec, pageable));
        return "index";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Long id, Model model,
                         HttpServletRequest request, HttpServletResponse response,
                         @AuthenticationPrincipal PrincipalDetail principal,
                         @RequestParam(value = "category", defaultValue = "none") String category,
                         @RequestParam(value = "page", defaultValue = "0") String page,
                         @RequestParam(value = "sort", defaultValue = "id, DESC") String sort,
                         @RequestParam(value = "searchType", defaultValue = "title") String searchType,
                         @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
        model.addAttribute("board", boardService.detail(id, request, response, principal.getUser().getId()));
        model.addAttribute("page", page);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        return "board/detail";
    }

    @GetMapping("/board/writeForm")
    public String writeForm() {
        return "board/writeForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("board", boardService.detail(id, null, null, principal.getUser().getId()));
        return "board/updateForm";
    }
}