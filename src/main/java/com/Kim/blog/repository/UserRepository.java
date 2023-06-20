package com.Kim.blog.repository;

import com.Kim.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {


    User findByUsernameAndPassword(String username, String password);
}
