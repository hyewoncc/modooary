package com.modooary.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class DiaryReply {

    @Id
    @GeneratedValue
    @Column(name = "DIARYREPLY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DIARYPOST_ID")
    private DiaryPost diaryPost;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String content;
    private LocalDateTime regdate;

    private void setDiaryPost(DiaryPost diaryPost) {
        this.diaryPost = diaryPost;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setCreateTime() {
        this.regdate = LocalDateTime.now();
    }

    /* 생성 메서드 */
    public static DiaryReply createDiaryReply (DiaryPost diaryPost, Member member, String content) {
        DiaryReply diaryReply = new DiaryReply();
        diaryReply.setDiaryPost(diaryPost);
        diaryReply.setMember(member);
        diaryReply.setContent(content);
        diaryReply.setCreateTime();

        return diaryReply;
    }
}
