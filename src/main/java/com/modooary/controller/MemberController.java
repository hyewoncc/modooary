package com.modooary.controller;

import com.modooary.controller.form.MemberForm;
import com.modooary.domain.PreMember;
import com.modooary.service.MemberService;
import com.modooary.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailUtil emailUtil;

    @GetMapping("/sign-in")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String sendSignInMail(@Valid MemberForm memberForm, BindingResult result) {

        //공란이 있을 시 재작성
        if (result.hasErrors()) {
            return "signIn";
        }

        //입력 정보를 받아 임시 회원으로 저장
        PreMember preMember = PreMember.createPreMember(
                memberForm.getEmail(), memberForm.getPassword(), memberForm.getName());
        memberService.joinPreMember(preMember);

        //임시 회원 정보를 바탕으로 확인 이메일 발송 후 메인으로 리다이렉트
        try {
            emailUtil.sendMail(
                    preMember.getName(), preMember.getEmail(), preMember.getId(), preMember.getKey());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    //인증 메일 링크로 들어올 시 파라미터 값을 DB와 비교하여 일치 시 정회원으로 등록
    @GetMapping("/sign-up/confirm")
    public String signUpConfirm(
            @RequestParam("id") Long id, @RequestParam("key") String key) {

        //임시 회원의 키값 조회 후 일치시 정회원 전환
        if (memberService.checkPreMemberKey(id, key)) {
            memberService.approveMember(id);
        }

        return "redirect:/";
    }
}
