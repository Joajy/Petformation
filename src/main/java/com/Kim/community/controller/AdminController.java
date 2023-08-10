package com.Kim.community.controller;


import com.Kim.community.config.auth.PrincipalDetail;
import com.Kim.community.model.RoleType;
import com.Kim.community.model.User;
import com.Kim.community.repository.BoardRepository;
import com.Kim.community.repository.UserRepository;
import com.Kim.community.specification.AdminSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @GetMapping("/admin")
    public String admin(Model model,
                        @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "category", defaultValue = "user") String category,
                        @RequestParam(value = "searchType", defaultValue = "") String searchType,
                        @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                        @AuthenticationPrincipal PrincipalDetail principal) {

        /* 관리자 권한이 아닌 경우 해당 페이지를 요청하지 못하도록 설정 */
        if(!principal.getUser().getRole().equals(RoleType.ADMIN)) return null;

        /* Specification을 사용하여 쿼리 조건 추가 */
        Specification<User> spec = (root, query, criteriaBuilder) -> null;
        spec = spec.and(AdminSpecification.userRole(RoleType.USER));

        if(category.equals("user")) {
            if(!searchType.isEmpty()) {
                if(searchType.equals("username")) {
                    spec = spec.and(AdminSpecification.searchTypeUsername(searchKeyword));
                } else {
                    spec = spec.and(AdminSpecification.searchTypeNickname(searchKeyword));
                }
            }

            model.addAttribute("users", userRepository.findAll(spec, pageable));
        } else {
            model.addAttribute("countToday", boardRepository.countTodayBoard());
            model.addAttribute("countYesterday", boardRepository.countYesterdayBoard());
            model.addAttribute("countTotal", boardRepository.countTotalBoard());

            model.addAttribute("countNone", boardRepository.countByCategory("none"));
            model.addAttribute("countSecret", boardRepository.countByCategory("secret"));
            model.addAttribute("countScreenshot", boardRepository.countByCategory("screenshot"));
            model.addAttribute("countQuestion", boardRepository.countByCategory("question"));
        }

        model.addAttribute("category", category);

        return "admin";
    }
}
