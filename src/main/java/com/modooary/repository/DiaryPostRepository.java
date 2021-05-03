package com.modooary.repository;

import com.modooary.domain.DiaryPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryPostRepository {

    private final EntityManager em;

    //새 포스트 저장
    @Transactional
    public void save(DiaryPost diaryPost) {
        em.persist(diaryPost);
    }

    //단일 포스트 삭제
    @Transactional
    public void delete(DiaryPost diaryPost) {
        em.remove(diaryPost);
    }

    //단일 포스트 조회
    @Transactional
    public DiaryPost findOne(Long id) {
        return em.find(DiaryPost.class, id);
    }

    //특정 다이어리에 속한 모든 포스트 조회
    public List<DiaryPost> findDiaryPosts(Long diaryId){
        return em.createQuery("select dp from DiaryPost dp" +
                " where dp.diary.id = :diaryId", DiaryPost.class)
                .setParameter("diaryId", diaryId)
                .getResultList();
    }
}
