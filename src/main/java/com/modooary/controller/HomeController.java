package com.modooary.controller;

import com.modooary.controller.form.LoginForm;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "main";
    }

    @PostMapping("/login")
    public String memberLogin(@Valid LoginForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "main";
        }

        if(memberService.memberLogin(form.getEmail(), form.getPassword())) {
            return "home";
        }else {
            return "redirect:/";
        }
    }


}
