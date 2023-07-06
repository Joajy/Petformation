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

    @JsonIgnoreProperties({"board", "reply"})
    @ManyToOne      //Board : User == Many : One
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

    //mappedBy 연관관계의 주인이 아님을 표시 -> 외래키가 아님
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"board", "user"})
    @OrderBy("id desc")
    private List<Reply> reply;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Recommend> recommend;

    private boolean recommendState;

    private int recommendCount;

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(createDate);
    }
}
