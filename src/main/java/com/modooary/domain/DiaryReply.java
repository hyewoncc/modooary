package com.modooary.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DiaryReply {

    @Id
    @GeneratedValue
    @Column(name = "DIARYREPLY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private DiaryReply parent;

    @OneToMany(mappedBy = "parent")
    private List<DiaryReply> child = new ArrayList<>();

    private String content;
    private LocalDateTime regdate;
}
