package com.modooary.controller;

import com.modooary.domain.Diary;
import com.modooary.domain.Invitation;
import com.modooary.domain.Member;
import com.modooary.service.DiarySetService;
import com.modooary.service.InvitationService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class InvitationController {

    private final MemberService memberService;
    private final DiarySetService diarySetService;
    private final InvitationService invitationService;

    @PostMapping("/send-invitation")
    @ResponseBody
    public void sendInvitation(HttpServletRequest request) {
        //세션에서 현재 사용자 id값을 받아 사용자 설정
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //세션에서 현재 다이어리 id값을 받아 다이어리 설정
        Long diaryId = (Long) session.getAttribute("diaryId");
        Diary diary = diarySetService.findDairy(diaryId);

        //ajax로 받은 id 값으로 초대장 보낼 회원을 찾음
        Long receiverId = Long.parseLong(request.getParameter("memberId"));
        Member receiverMember = memberService.findOneMember(receiverId);

        //초대장을 생성하고 저장
        Invitation invitation = Invitation.createInvitation(member, receiverMember, diary);
        invitationService.registerInvitation(invitation);
    }

    @PostMapping("/accept-invitation")
    @ResponseBody
    public void acceptInvitation(HttpServletRequest request) {
        //전송된 초대장 id값을 받아와서 찾음
        Long invitationId = Long.parseLong(request.getParameter("invitationId"));
        Invitation invitation = invitationService.findOne(invitationId);

        //초대장 정보를 바탕으로 회원을 다이어리에 등록
        diarySetService.registerDiaryMember(invitation.getDiary(), invitation.getReceiver());

        //초대장 삭제
        invitationService.deleteInvitation(invitation);
    }

    @PostMapping("/reject-invitation")
    @ResponseBody
    public void rejectInvitation(HttpServletRequest request) {
        //전송된 초대장 id값을 받아와서 찾음
        Long invitationId = Long.parseLong(request.getParameter("invitationId"));
        Invitation invitation = invitationService.findOne(invitationId);

        //초대장 삭제
        invitationService.deleteInvitation(invitation);
    }

}
