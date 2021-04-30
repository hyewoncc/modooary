package com.modooary.repository;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryMemberRepository {

    private final EntityManager em;

    //다이어리-회원 소속 정보 저장
    @Transactional
    public void save(DiaryMember diaryMember){
        em.persist(diaryMember);
    }

    //단일 다이어리에 속한 모든 회원 찾기
    public List<DiaryMember> findDairyMembers(Long dairyId){
        return em.createQuery("select dm from DiaryMember dm" +
                " where dm.diary.id = :dairyId", DiaryMember.class)
                .setParameter("dairyId", dairyId)
                .getResultList();
    }

    //단일 회원의 모든 다이어리 찾기
    public List<DiaryMember> findMemberDairies(Long memberId){
        return em.createQuery("select dm from DiaryMember dm" +
                " where dm.member.id = :memberId", DiaryMember.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
