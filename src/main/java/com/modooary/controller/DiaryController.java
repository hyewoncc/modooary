package com.modooary.controller;

import com.modooary.controller.dto.PostReplyDto;
import com.modooary.domain.*;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.DiarySetService;
import com.modooary.service.InvitationService;
import com.modooary.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DiaryController {

    private final MemberService memberService;
    private final DiaryBoardService diaryBoardService;
    private final DiarySetService diarySetService;
    private final InvitationService invitationService;

    @GetMapping("/diary")
    public String diaryHome(HttpSession session, Model model) {

        //세션에서 현재 사용자를 받아옴
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //현재 사용자의 모든 다이어리 목록 찾기
        List<DiaryMember> diaryMembers = new ArrayList<>();
        diaryMembers = member.getDiaryMembers();

        //다이어리가 하나 이상이라면 안내 다이어리를 빼기
        if(diaryMembers.size() > 1) {
            diaryMembers.remove(0);
        }

        //현재 사용자의 첫번째 다이어리 (가장 오래 된 다이어리)로 이동
        Diary diary = diaryMembers.get(0).getDiary();
        return "redirect:/diary/" + diary.getId();
    }

    //새로운 다이어리 생성 또는 수정
    @PostMapping("/diary")
    public String setDiary(HttpServletRequest request) {

        //작성된 다이어리 이름과 색상 정보를 얻어옴
        String diaryTitle = request.getParameter("new-diary-title");
        String colorCode = request.getParameter("color-code");
        Long diaryId;

        //새 다이어리 생성인지, 기존 다이어리 수정인지 확인
        String purpose = request.getParameter("form-purpose");

        //새 다이어리 생성 또는 정보 수정 후 해당 다이어리로 이동
        if(purpose.equals("create")){
            diaryId = createDiary(request.getSession(), diaryTitle, colorCode);
        } else{
            //다이어리를 id로 찾아 정보 수정
            diaryId = Long.parseLong(request.getParameter("diary-id"));
            Diary diary = diarySetService.findDairy(diaryId);
            diarySetService.changeDiaryInfo(diary, diaryTitle, colorCode);
        }
        return "redirect:/diary/" + diaryId;
    }

    //다이어리 생성 메소드
    private Long createDiary(HttpSession session, String title, String color) {
        //세션에서 현재 사용자를 받아옴
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //현재 사용자를 HOST로 새로운 다이어리 개설 후 색상 적용
        Diary diary = Diary.createDiary(title);
        diary.changeColor(color);
        Long diaryId = diarySetService.registerDiary(diary, member);

        //현재 사용자를 작성자로 첫 게시글 자동 등록
        DiaryPost diaryPost = DiaryPost.createPost(diary, member,
                member.getName() + "님이 " + diary.getTitle() + " 모두어리를 개설했습니다!" +
                "\n친구를 초대해 모두 함께 이야기를 나눠보세요");
        diaryBoardService.registerDiaryPost(diaryPost);

        return diaryId;
    }

    //특정 다이어리 보여주기
    @GetMapping("/diary/{diaryId}")
    public String diaryBoard(@PathVariable("diaryId") Long diaryId, HttpSession session, Model model) {

        //세션에서 현재 사용자를 받아옴
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);
        //모델에 현재 사용자 추가
        model.addAttribute("member", member);

        //이 다이어리가 존재하지 않는 다이어리거나,
        //현재 사용자가 소속 회원이 아니라면 되돌아가기 처리
        if(!(diaryExistCheck(diaryId) &&
                diarySetService.checkMemberInDiary(member, diarySetService.findDairy(diaryId)))){
            return "redirect:/";
        }

        //현재 사용자의 모든 다이어리 목록 찾기
        List<DiaryMember> diaryMemberForDiary = member.getDiaryMembers();

        //다이어리가 2개 이상이면 처음 안내 다이어리는 삭제
        if(diaryMemberForDiary.size() > 1) {
            diaryMemberForDiary.remove(0);
        }

        //현재 사용자의 모든 다이어리 찾기
        List<Diary> diaries = new ArrayList<>();
        for (DiaryMember dm : diaryMemberForDiary) {
            diaries.add(dm.getDiary());
        }
        //모델에 다이어리 목록 추가
        model.addAttribute("diaries", diaries);


        //현재 다이어리의 페이징된 포스트 중 첫페이지를 조
        Diary diary = diarySetService.findDairy(diaryId);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("regdate").descending());
        Page<DiaryPost> diaryPosts = diaryBoardService.loadMorePosts(diary, pageable);
        //모델에 다이어리와 포스트 목록 추가
        model.addAttribute("diary", diary);
        model.addAttribute("posts", diaryPosts);


        //모든 포스트의 댓글 조회
        Map<Long, List<PostReplyDto>> replyMap = new HashMap<>();
        for (DiaryPost dp : diaryPosts) {
            List<PostReply> postReplies = diaryBoardService.listPostReplies(dp);
            List<PostReplyDto> postReplyDtos = postReplies.stream()
                    .map(p -> new PostReplyDto(p))
                    .collect(Collectors.toList());
            replyMap.put(dp.getId(), postReplyDtos);
        }
        //모델에 댓글 맵 추가
        model.addAttribute("replyMap", replyMap);


        //현재 다이어리의 모든 멤버 조회
        List<DiaryMember> members = diary.getDiaryMembers();
        //모델에 멤버 목록 추가
        model.addAttribute("members", members);
        //현재 다이어리에서 내 권한 찾아서 모델에 추가
        for (DiaryMember diaryMember : members) {
            if (diaryMember.getMember().equals(member) && diaryMember.getGrade().equals(Grade.HOST)){
                    model.addAttribute("host", true);
                }
            }

        //현재 회원의 모든 초대장 조회
        List<Invitation> invitations = invitationService.findInvitations(memberId);
        List<InvitationDto> invitationDtos = invitations.stream()
                .map(i -> new InvitationDto(i))
                .collect(Collectors.toList());
        //모델에 초대장 목록 추가
        model.addAttribute("invitations", invitationDtos);


        //세션에 현재 다이어리의 id값 추가
        session.setAttribute("diaryId", diaryId);

        return "diary";
    }

    @ResponseBody
    @GetMapping("/diary/{diaryId}/load-post")
    public List<DiaryPostDto> loadMorePosts(
            @PathVariable("diaryId") Long diaryId, @RequestParam int page, @RequestParam int size) {

        Diary dairy = diarySetService.findDairy(diaryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("regdate").descending());
        Page<DiaryPost> diaryPosts = diaryBoardService.loadMorePosts(dairy, pageable);
        List<DiaryPostDto> diaryPostDtos = diaryPosts.stream()
                .map(p -> new DiaryPostDto(p))
                .collect(Collectors.toList());

        return diaryPostDtos;
    }

    @Data
    @AllArgsConstructor
    static class InvitationDto {
        private Long id;
        private String sender_name;
        private String sender_picture;
        private String receiver_name;
        private String diary_title;

        public InvitationDto(Invitation invitation) {
            id = invitation.getId();
            sender_name = invitation.getSender().getName();
            sender_picture = invitation.getSender().getPicture();
            receiver_name = invitation.getReceiver().getName();
            diary_title = invitation.getDiary().getTitle();
        }
    }

    //특정 번호의 다이어리가 존재하는 다이어리인지 확인
    private boolean diaryExistCheck(Long diaryId){
        if(diarySetService.findDairy(diaryId) != null){
            return true;
        }else {
            return false;
        }
    }

    @Data
    @AllArgsConstructor
    static class DiaryPostDto {
        private Long id;
        private Long diary_id;
        private String member_name;
        private String member_picture;
        private String content;
        private String regdate;

        public DiaryPostDto(DiaryPost diaryPost) {
            id = diaryPost.getId();
            diary_id = diaryPost.getDiary().getId();
            member_name = diaryPost.getMember().getName();
            member_picture = diaryPost.getMember().getPicture();
            content = diaryPost.getContent();
            regdate = diaryPost.getRegdate().format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
        }
    }
}
