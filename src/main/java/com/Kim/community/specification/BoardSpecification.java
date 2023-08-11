package com.Kim.community.specification;

import com.Kim.community.model.Board;
import org.springframework.data.jpa.domain.Specification;

public class BoardSpecification {

    // 글 제목에 따른 검색
    public static Specification<Board> searchTypeTitle(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + searchKeyword + "%");
    }

    // 작성자에 따른 검색
    public static Specification<Board> searchTypeWriter(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("nickname"), searchKeyword);
    }

    // 카테고리 구분
    public static Specification<Board> equalCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    // 추천 수에 따른 구분
    public static Specification<Board> recGreaterThan(int recommendCount) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("recommendCount"), recommendCount);
    }
}
