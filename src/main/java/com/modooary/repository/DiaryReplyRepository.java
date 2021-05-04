package com.modooary.repository;

import com.modooary.domain.DiaryReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryReplyRepository {

    private final EntityManager em;

    //새 댓글 저장
    @Transactional
    public void save(DiaryReply diaryReply) {
        em.persist(diaryReply);
    }

    //단일 댓글 삭제
    @Transactional
    public void delete(DiaryReply diaryReply) {
        em.remove(diaryReply);
    }

    //단일 댓글 조회
    public DiaryReply findOne(Long id) {
        return em.find(DiaryReply.class, id);
    }

    //특정 포스트에 속한 모든 댓글 조회
    public List<DiaryReply> findPostReplies(Long postId) {
        return em.createQuery("select dr from DiaryReply dr" +
                " where dr.diaryPost.id = :postId", DiaryReply.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
