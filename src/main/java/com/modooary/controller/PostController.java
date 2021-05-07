package com.modooary.controller;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryPost;
import com.modooary.domain.Member;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.DiarySetService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final DiaryBoardService diaryBoardService;
    private final DiarySetService diarySetService;
    private final MemberService memberService;

    @PostMapping("/post")
    public String uploadPost(HttpServletRequest request) {
        HttpSession session = request.getSession();

        //현재 사용자와 다이어리 값 세션에서 받아 찾기
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);
        Long diaryId = (Long) session.getAttribute("diaryId");
        Diary diary = diarySetService.findDairy(diaryId);

        //입력된 포스트 내용을 받아서 포스트 객체 생성
        String postText = request.getParameter("post_text");
        DiaryPost diaryPost = DiaryPost.createPost(diary, member, postText);
        diaryBoardService.registerDiaryPost(diaryPost);

        //다이어리로 돌아가기
        return "redirect:/diary/" + diary.getId();
    }
}
