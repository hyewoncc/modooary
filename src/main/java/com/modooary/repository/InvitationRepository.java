package com.modooary.repository;

import com.modooary.domain.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvitationRepository {

    private final EntityManager em;

    //새 초대장 저장
    @Transactional
    public void save(Invitation invitation) {
        em.persist(invitation);
    }

    //단일 초대장 삭제
    @Transactional
    public void delete(Invitation invitation) {
        em.remove(invitation);
    }

    //특정 회원이 받은 모든 초대장 조회
    public List<Invitation> findInvitations(Long memberId) {
        return em.createQuery("select i from Invitation i" +
                " where i.receiver.id = :memberId")
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
