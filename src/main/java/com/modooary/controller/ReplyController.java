package com.modooary.controller;

import com.modooary.controller.dto.PostReplyDto;
import com.modooary.domain.DiaryPost;
import com.modooary.domain.Member;
import com.modooary.domain.PostReply;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final MemberService memberService;
    private final DiaryBoardService diaryBoardService;

    @PostMapping("/reply")
    @ResponseBody
    public List<PostReplyDto> registerReply(HttpServletRequest request) {

        //세션에서 현재 사용자 id값을 받아 사용자 설정
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //비동기 통신으로 받은 데이터를 이용해 댓글 저장
        Long postId = Long.parseLong(request.getParameter("postId"));
        DiaryPost diaryPost = diaryBoardService.findOnePost(postId);
        String reply = request.getParameter("reply");
        PostReply postReply = PostReply.createPostReply(diaryPost, member, reply);
        diaryBoardService.registerPostReply(postReply);

        //댓글을 작성한 포스트의 id값으로 댓글을 다시 읽어오기
        List<PostReply> postReplies = diaryBoardService.listPostReplies(diaryPost);
        List<PostReplyDto> postReplyDtos = postReplies.stream()
                .map(p -> new PostReplyDto(p))
                .collect(Collectors.toList());

        return postReplyDtos;
    }
}
