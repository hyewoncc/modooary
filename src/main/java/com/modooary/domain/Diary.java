package com.modooary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue
    @Column(name = "DIARY_ID")
    private Long id;

    private String title;
    private LocalDateTime regdate;

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
}
