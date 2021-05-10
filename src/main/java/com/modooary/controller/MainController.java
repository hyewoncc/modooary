package com.modooary.controller;

import com.modooary.controller.form.LoginForm;
import com.modooary.domain.Member;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String main(Model model, HttpSession session) {
        model.addAttribute("loginForm", new LoginForm());
        if (!model.containsAttribute("emailAlert")) {
            model.addAttribute("emailAlert", "");
        }

        //이미 로그인 한 사람이라면 다이어리 페이지로 이동시킴
        if (session.getAttribute("memberId") != null) {
            return "redirect:/diary";
        }
        return "main";
    }

    @PostMapping("/login")
    public String memberLogin(@Valid LoginForm form, BindingResult result,
                              HttpSession session) {

        if (result.hasErrors()) {
            return "main";
        }

        if (memberService.memberLogin(form.getEmail(), form.getPassword())) {
            Member member = memberService.findOneByEmail(form.getEmail());
            session.setAttribute("memberId", member.getId());
            return "redirect:/diary";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String memberLogout(HttpSession session){
        session.removeAttribute("memberId");

        return "redirect:/";
    }
}
