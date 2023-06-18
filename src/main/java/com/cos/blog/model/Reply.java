package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne      //Reply : Board == Many : One
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne      //Reply : User == Many : One
    @JoinColumn(name = "userId")
    private User user;

    @CreatedDate
    private LocalDateTime createDate;

}
