package com.Kim.blog.model;


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
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob                //대용량 데이터 처리 대비
    private String content;

    private int count;

    @JsonIgnoreProperties({"boards", "replys"})
    @ManyToOne      //Board : User == Many : One
    @JoinColumn(name = "user_id")
    private User user;

    private String userNickname;

    @CreationTimestamp
    private Timestamp createDate;

    //mappedBy 연관관계의 주인이 아님을 표시 -> 외래키가 아님
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"board", "user"})
    @OrderBy("id desc")
    private List<Reply> replys = new ArrayList<>();

    @JsonIgnoreProperties({"board", "user"})
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Recommend> recommends;

    private boolean recommendState;

    private int recommendCount;

    @Transient
    private Board prevBoard;

    @Transient
    private Board nextBoard;

    private String seen;

    private String category;

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(createDate);
    }
}
