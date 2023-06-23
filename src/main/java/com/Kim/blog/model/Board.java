package com.Kim.blog.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob                //대용량 데이터 처리 대비
    private String content;

    private int count;

    @ManyToOne      //Board : User == Many : One
    @JoinColumn(name = "userId")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

    //mappedBy 연관관계의 주인이 아님을 표시 -> 외래키가 아님
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Reply> reply;
}
