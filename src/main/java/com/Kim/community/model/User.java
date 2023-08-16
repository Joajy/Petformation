package com.Kim.community.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;
    
    @CreationTimestamp
    private Timestamp createDate;

    private String oauth;

    private String profileImageUrl;

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy.MM.dd").format(createDate);
    }

    @JsonIgnoreProperties({"user"})
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();
}
