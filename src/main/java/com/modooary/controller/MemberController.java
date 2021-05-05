package com.modooary.controller;

import com.modooary.domain.PreMember;
import com.modooary.service.MemberService;
import com.modooary.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

        try {
            emailUtil.sendMail(preMember.getEmail(), "모두어리 가입", "가입하세요");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }
}
