package com.modooary.controller;

import com.modooary.domain.*;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.DiarySetService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DiaryController {

    private final MemberService memberService;
    private final DiaryBoardService diaryBoardService;
    private final DiarySetService diarySetService;

    @GetMapping("/diary")
    public String diaryHome(HttpSession session) {

        //세션에서 현재 사용자를 받아옴
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //현재 사용자의 모든 다이어리 목록 찾기
        List<DiaryMember> diaryMembers = member.getDiaryMembers();

        //다이어리가 없는 신규 회원이라면 따로 처리
        if(diaryMembers.size() == 0) {
            return "diary";
        }else {
            //현재 사용자의 첫번째 다이어리 (가장 오래 된 다이어리)로 이동
            Diary diary = diaryMembers.get(0).getDiary();

            return "redirect:/diary/" + diary.getId();
        }
    }

    //새로운 다이어리 생성
    @PostMapping("/diary")
    public String registerDiary(HttpServletRequest request, Model model) {

        //세션에서 현재 사용자를 받아옴
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //입력된 다이어리 제목 정보를 받아옴
        String diaryTitle = request.getParameter("add-diary-title");

        //현재 사용자를 HOST로 새로운 다이어리 개설
        Diary diary = Diary.createDiary(diaryTitle);
        Long diaryId = diarySetService.registerDiary(diary, member);

        //새로운 다이어리로 이동
        return "redirect:/diary/" + diaryId;
    }

    //특정 다이어리 보여주기
    @GetMapping("/diary/{diaryId}")
    public String diaryBoard(@PathVariable("diaryId") Long diaryId, HttpSession session, Model model) {

        //세션에서 현재 사용자를 받아옴
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //현재 사용자의 모든 다이어리 목록 찾기
        List<DiaryMember> diaryMemberForDiary = member.getDiaryMembers();
        //현재 사용자의 모든 다이어리 찾기
        List<Diary> diaries = new ArrayList<>();
        for (DiaryMember dm : diaryMemberForDiary) {
            diaries.add(dm.getDiary());
        }

        //모델에 다이어리 목록 추가
        model.addAttribute("diaries", diaries);

        //현재 다이어리의 모든 포스트 조회
        Diary diary = diarySetService.findDairy(diaryId);
        List<DiaryPost> diaryPosts = diaryBoardService.listDiaryPosts(diary);

        //모델에 다이어리와 포스트 목록 추가
        model.addAttribute("diary", diary);
        model.addAttribute("posts", diaryPosts);

        //모든 포스트의 댓글 조회
        Map<Long, List<PostReply>> replyMap = new HashMap<>();
        for (DiaryPost dp : diaryPosts) {
            replyMap.put(dp.getId(), diaryBoardService.listPostReplies(dp));
        }

        //모델에 댓글 맵 추가
        model.addAttribute("replyMap", replyMap);

        //현재 다이어리의 모든 멤버 조회
        List<DiaryMember> members = diary.getDiaryMembers();

        //모델에 멤버 목록 추가
        model.addAttribute("members", members);

        //세션에 현재 다이어리의 id값 추가
        session.setAttribute("diaryId", diaryId);

        return "diary";
    }
}
