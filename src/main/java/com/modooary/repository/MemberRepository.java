package com.modooary.repository;

import com.modooary.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    //단일 이메일로 조회
    public Member findOneByEmail(String email) {
        return em.createQuery("select m from Member m" +
                " where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    //중복되는 이메일이 있는지 검사
    public boolean checkEmail(String email) {
        try{
            Member member = em.createQuery("select m from Member m" + "" +
                    " where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }catch (NoResultException e) {
            return true;
        }
        return false;
    }

}
