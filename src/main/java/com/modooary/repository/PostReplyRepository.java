package com.modooary.repository;

import com.modooary.domain.PostReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReplyRepository {

    private final EntityManager em;

    //새 댓글 저장
    @Transactional
    public void save(PostReply postReply) {
        em.persist(postReply);
    }

    //단일 댓글 삭제
    @Transactional
    public void delete(PostReply postReply) {
        em.remove(postReply);
    }

    //단일 댓글 조회
    public PostReply findOne(Long id) {
        return em.find(PostReply.class, id);
    }

    //특정 포스트에 속한 모든 댓글 조회
    public List<PostReply> findPostReplies(Long postId) {
        return em.createQuery("select dr from PostReply dr" +
                " where dr.diaryPost.id = :postId", PostReply.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
