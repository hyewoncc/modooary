package com.modooary.repository;

import com.modooary.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepository {

    private final EntityManager em;

    //다이어리 게시판 생성
    public void save(Diary diary){
        em.persist(diary);
    }
}
