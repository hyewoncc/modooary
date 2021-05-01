package com.modooary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
public class DiaryMember {

    @Id
    @GeneratedValue
    @Column(name = "DIARY_MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    protected DiaryMember(){}

    private void setMember(Member member) {
        this.member = member;
    }

    private void setDiary(Diary diary) {
        this.diary = diary;
    }

    private void setHost() {
        this.grade = Grade.HOST;
    }

    private void setGuest() {
        this.grade = Grade.GUEST;
    }

    /* 생성 메서드 */
    //회원이 새 다이어리를 개설해서 관리자가 될 때
    public static DiaryMember createHost(Member member, Diary diary){
        DiaryMember diaryMember = new DiaryMember();
        diaryMember.setMember(member);
        diaryMember.setDiary(diary);
        diaryMember.setHost();

        return diaryMember;
    }

    //관리자 회원이 다른 회원을 다이어리로 초대했을 때
    public static DiaryMember createGuest(Member member, Diary diary){
        DiaryMember diaryMember = new DiaryMember();
        diaryMember.setMember(member);
        diaryMember.setDiary(diary);
        diaryMember.setGuest();

        return diaryMember;
    }



}
