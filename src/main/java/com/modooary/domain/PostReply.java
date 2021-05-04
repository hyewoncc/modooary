package com.modooary.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class PostReply {

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
    public static PostReply createDiaryReply (DiaryPost diaryPost, Member member, String content) {
        PostReply postReply = new PostReply();
        postReply.setDiaryPost(diaryPost);
        postReply.setMember(member);
        postReply.setContent(content);
        postReply.setCreateTime();

        return postReply;
    }
}
