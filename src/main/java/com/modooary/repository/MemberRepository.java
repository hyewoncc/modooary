package com.modooary.repository;

import com.modooary.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //회원 정보 저장
    public void save(Member member){
        em.persist(member);
    }

    //단일 회원 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //모든 회원 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
