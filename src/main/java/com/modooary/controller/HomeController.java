package com.modooary.controller;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryMember;
import com.modooary.domain.Member;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final DiaryBoardService diaryBoardService;

    @GetMapping("/diary")
    public String diaryHome(HttpSession session, Model model) {
        Long memberId = (Long)session.getAttribute("memberId");
        Member oneMember = memberService.findOneMember(memberId);
        List<DiaryMember> diaryMembers = oneMember.getDiaryMembers();
        List<Diary> diaries = new ArrayList<>();
        for (DiaryMember dm : diaryMembers) {
            diaries.add(dm.getDiary());
        }

        model.addAttribute("diaryList", diaries);

        return "diary";
    }
}
