package com.modooary.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class DiaryPost {

    @Id
    @GeneratedValue
    @Column(name = "DIARYPOST_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String content;
    private LocalDateTime regdate;
}
