package com.modooary.controller;

import com.modooary.domain.DiaryPost;
import com.modooary.domain.Member;
import com.modooary.domain.PostReply;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final MemberService memberService;
    private final DiaryBoardService diaryBoardService;

    @PostMapping("/reply")
    @ResponseBody
    public postReplyDto registerReply(HttpServletRequest request) {

        //세션에서 현재 사용자 id값을 받아 사용자 설정
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //비동기 통신으로 받은 데이터를 이용해 댓글 객체 생성
        Long postId = Long.parseLong(request.getParameter("postId"));
        DiaryPost diaryPost = diaryBoardService.findOnePost(postId);
        String reply = request.getParameter("reply");
        PostReply postReply = PostReply.createPostReply(diaryPost, member, reply);
        diaryBoardService.registerPostReply(postReply);

        return new postReplyDto(member.getName(), postReply.getContent());
    }


    @Data
    @AllArgsConstructor
    static class postReplyDto {
        private String name;
        private String content;
    }

}
