package com.modooary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Diary {

    @Id
    @GeneratedValue
    @Column(name = "DIARY_ID")
    private Long id;

    private String title;
    private LocalDateTime regdate;
}
