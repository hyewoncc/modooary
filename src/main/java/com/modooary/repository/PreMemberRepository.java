package com.modooary.repository;

import com.modooary.domain.PreMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PreMemberRepository {

    private final EntityManager em;

    //임시 회원 정보 저장
    public void save(PreMember preMember) {
        em.persist(preMember);
    }

    //단일 임시 회원 조회
    public PreMember findOne(Long id) {
        return em.find(PreMember.class, id);
    }

    //임시 회원 삭제
    public void delete(PreMember preMember) {
        em.remove(preMember);
    }
}
