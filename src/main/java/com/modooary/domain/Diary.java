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
    private LocalDateTime newdate;

    protected Diary(){};
}
