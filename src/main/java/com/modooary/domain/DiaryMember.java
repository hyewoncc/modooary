package com.modooary.domain;

import javax.persistence.*;

@Entity
public class DiaryMember {

    @Id
    @GeneratedValue
    @Column(name = "DIARY_MEMBER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @Enumerated(EnumType.STRING)
    private Grade grade;

}
