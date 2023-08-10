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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @JsonIgnoreProperties({"user", "replys"})
    @ManyToOne(fetch = FetchType.LAZY)      //Reply : Board == Many : One
    @JoinColumn(name = "board_id")
    private Board board;

    @JsonIgnoreProperties({"boards", "replys"})
    @ManyToOne(fetch = FetchType.LAZY)      //Reply : User == Many : One
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

    private boolean alarmConfirmState;

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(createDate);
    }
}