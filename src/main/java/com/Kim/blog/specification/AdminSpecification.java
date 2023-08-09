package com.Kim.blog.specification;

import com.Kim.blog.model.RoleType;
import com.Kim.blog.model.User;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecification {

    public static Specification<User> searchTypeUsername(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + searchKeyword + "%");
    }

    public static Specification<User> searchTypeNickname(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nickname"), "%" + searchKeyword + "%");
    }

    public static Specification<User> userRole(RoleType role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }
}
