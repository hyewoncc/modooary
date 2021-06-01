package com.modooary.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class DiaryPost {

    @Id
    @GeneratedValue
    @Column(name = "DIARYPOST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "diaryPost", cascade = CascadeType.ALL)
    private List<PostReply> postReplies = new ArrayList<>();

    @Column(length = 512)
    private String content;
    private LocalDateTime regdate;

    private void setDiary(Diary diary){
        this.diary = diary;
    }

    private void setMember(Member member){
        this.member = member;
    }

    private void setContent(String content){
        this.content = content;
    }

    private void setCreateTime(){
        this.regdate = LocalDateTime.now();
    }

    protected DiaryPost(){}

    /* 생성 메서드 */
    public static DiaryPost createPost(Diary diary, Member member, String content){
        DiaryPost diaryPost = new DiaryPost();
        diaryPost.setDiary(diary);
        diaryPost.setMember(member);
        diaryPost.setContent(content);
        diaryPost.setCreateTime();

        return diaryPost;
    }
}
