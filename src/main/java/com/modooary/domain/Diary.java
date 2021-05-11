package com.modooary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue
    @Column(name = "DIARY_ID")
    private Long id;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryMember> diaryMembers = new ArrayList<>();

    private String title;
    private LocalDateTime regdate;
    private String color;

    protected Diary(){};

    private void setTitle(String title){
        this.title = title;
    }

    private void setCreateTime(){
        this.regdate = LocalDateTime.now();
    }

    /* 생성 메서드 */
    public static Diary createDiary(String title){
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setCreateTime();
        return diary;
    }

    //다이어리 색상 변경
    public void changeColor(String color) {
        this.color = color;
    }
}
