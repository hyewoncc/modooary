package com.modooary.service;

import com.modooary.domain.Invitation;
import com.modooary.repository.DiaryMemberRepository;
import com.modooary.repository.DiaryRepository;
import com.modooary.repository.InvitationRepository;
import com.modooary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    //초대장 등록
    @Transactional
    public Long registerInvitation(Invitation invitation) {
        invitationRepository.save(invitation);

        return invitation.getId();
    }

    //초대장 삭제
    @Transactional
    public void deleteInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    //회원에게 온 모든 초대장 조회
    public List<Invitation> findInvitations(Long memberId) {
        List<Invitation> invitations = new ArrayList<>();
        invitations = invitationRepository.findInvitations(memberId);
        return invitations;
    }
}
